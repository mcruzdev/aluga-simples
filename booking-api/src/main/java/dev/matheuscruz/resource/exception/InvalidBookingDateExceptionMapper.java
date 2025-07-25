package dev.matheuscruz.resource.exception;

import dev.matheuscruz.exception.InvalidBookingDateException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class InvalidBookingDateExceptionMapper implements ExceptionMapper<InvalidBookingDateException> {

    @Override
    public Response toResponse(InvalidBookingDateException exception) {
        return Response
                .status(400)
                .entity(Map.of("message", exception.getMessage()))
                .build();
    }
}
