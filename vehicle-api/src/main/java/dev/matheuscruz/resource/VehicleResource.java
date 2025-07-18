package dev.matheuscruz.resource;

import dev.matheuscruz.dto.CreateVehicleRequest;
import dev.matheuscruz.dto.UpdateVehicleStatusRequest;
import dev.matheuscruz.dto.VehicleResponse;
import dev.matheuscruz.model.Vehicle;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
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

        return Response.created(URI.create("api/v1/vehicles/" + vehicle.getId())).entity(vehicle).build();
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
                vehicle.setStatus(request.status());
            } catch (IllegalStateException e) {
                return Response.status(Response.Status.CONFLICT).build();
            }
            return Response.ok(new VehicleResponse(vehicle)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
