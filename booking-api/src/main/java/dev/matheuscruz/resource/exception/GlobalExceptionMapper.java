package dev.matheuscruz.resource.exception;

import dev.matheuscruz.exception.BookingWithConflictException;
import dev.matheuscruz.exception.InvalidBookingDateException;
import dev.matheuscruz.exception.VehicleNotFoundException;
import dev.matheuscruz.resource.dto.ProblemDTO;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@Provider
public class GlobalExceptionMapper {

    @ServerExceptionMapper
    public Response handleInvalidBookingDateException(InvalidBookingDateException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ProblemDTO(e.getMessage()))
                .build();
    }

    @ServerExceptionMapper
    public Response handleVehicleNotFoundException(VehicleNotFoundException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ProblemDTO(e.getMessage()))
                .build();
    }

    @ServerExceptionMapper
    public Response handleBookingWithConflictException(BookingWithConflictException e) {
        return Response.status(Response.Status.CONFLICT)
                .entity(new ProblemDTO(e.getMessage()))
                .build();
    }

    @ServerExceptionMapper
    public Response handleVehicleNotAvailableException(VehicleNotFoundException e) {
        return Response.status(Response.Status.CONFLICT)
                .entity(new ProblemDTO(e.getMessage()))
                .build();
    }

}
