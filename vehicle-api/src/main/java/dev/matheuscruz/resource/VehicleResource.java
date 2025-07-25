package dev.matheuscruz.resource;

import dev.matheuscruz.dto.AddAccessoryRequest;
import dev.matheuscruz.dto.CreateMaintenanceRequest;
import dev.matheuscruz.dto.CreateVehicleRequest;
import dev.matheuscruz.dto.UpdateVehicleStatusRequest;
import dev.matheuscruz.dto.VehicleResponse;
import dev.matheuscruz.model.Accessory;
import dev.matheuscruz.model.Maintenance;
import dev.matheuscruz.model.Vehicle;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Parameters;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Path("/api/v1/vehicles")
public class VehicleResource {

    @POST
    @Transactional
    public Response create(CreateVehicleRequest request) {

        Vehicle vehicle = new Vehicle(request.model(), request.year(), request.engine(), request.brand());

        vehicle.persist();

        VehicleResponse response = new VehicleResponse(vehicle);

        return Response.created(URI.create("api/v1/vehicles/" + vehicle.getId())).entity(response).build();
    }

    @GET
    public Response getAll() {
        List<Vehicle> list = Vehicle.listAll();
        List<VehicleResponse> response = list.stream()
                .map(VehicleResponse::new)
                .toList();
        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}")
    public Response getByID(@PathParam("id") Long id) {
        Optional<Vehicle> vehicle = Vehicle.findByIdOptional(id);
        Log.info("Recebendo request");
        if (vehicle.isPresent()) {
            return Response.ok(new VehicleResponse(vehicle.get())).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PATCH
    @Path("/{id}")
    @Transactional
    public Response updateStatusPartially(@PathParam("id") Long id, UpdateVehicleStatusRequest request) {

        Vehicle vehicle = Vehicle.findById(id);
        if (vehicle != null) {

            try {
                vehicle.changeStatus(request.status());
            } catch (IllegalStateException e) {
                return Response.status(Response.Status.CONFLICT).build();
            }
            return Response.ok(new VehicleResponse(vehicle)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteById(@PathParam("id") Long id) {
        Vehicle.deleteById(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/{vehicleId}/maintenances")
    @Transactional
    public Response addMaintenance(@PathParam("vehicleId") Long vehicleId, CreateMaintenanceRequest request) {

        Optional<Vehicle> possibleVehicle = Vehicle.findByIdOptional(vehicleId);

        if (possibleVehicle.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Vehicle vehicle = possibleVehicle.get();
        Maintenance maintenance = new Maintenance(
                request.problem(),
                vehicleId
        );

        maintenance.persist();

        vehicle.moveForMaintenance(maintenance);

        return Response.created(URI.create("/api/v1/vehicles/%d/maintenances/%d".formatted(
                vehicleId, maintenance.getId()
        ))).build();
    }

    @GET
    @Path("/{vehicleId}/maintenances/{maintenanceId}")
    public Response getMaintenanceById(@PathParam("vehicleId") Long vehicleId, @PathParam("maintenanceId") Long maintenanceId) {

        Maintenance maintenance = Maintenance.find("vehicleId = :vehicleId AND id = :maintenanceId", Parameters.with(
                "vehicleId", vehicleId).and("maintenanceId", maintenanceId
        )).firstResult();

        if (maintenance == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(maintenance).build();
        }
    }

    @PUT
    @Transactional
    @Path("/{id}/accessories")
    public Response addAccessory(@PathParam("id") Long id, AddAccessoryRequest request) {

        Vehicle vehicle = Vehicle.findById(id);

        if (vehicle == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Accessory accessory = new Accessory(request.name());

        accessory.persist();

        vehicle.addAccessory(accessory);

        Log.info(vehicle.getAccessories());

        return Response.noContent().build();

    }

}
