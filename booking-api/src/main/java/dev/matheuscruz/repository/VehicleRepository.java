package dev.matheuscruz.repository;

import dev.matheuscruz.exception.VehicleNotFoundException;
import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "vehicle-api")
public interface VehicleRepository {

    @GET
    @Path("/api/v1/vehicles/{vehicleId}")
    Vehicle findVehicleById(@PathParam("vehicleId") Long id);

    @ClientExceptionMapper
    static VehicleNotFoundException handler(Response response) {
        if (response.getStatus() == 404) {
            return new VehicleNotFoundException("Vehicle with ID not found");
        }
        return null;
    }

    record Vehicle(String status) {}
}