package dev.matheuscruz.resource;

import dev.matheuscruz.model.Booking;
import dev.matheuscruz.model.BookingStatus;
import dev.matheuscruz.repository.BookingDAO;
import dev.matheuscruz.service.BookingService;
import jakarta.inject.Inject;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;

@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingResource {

    private BookingService bookingService;

    public BookingResource(BookingService bookingService) {
        // Quarkus injeta pra mim um BookingService
        // eu não quero saber como ele vem, só quero usar isso aqui.
        this.bookingService = bookingService;
    }

    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") Long id) {
        Booking booking = bookingService.getBookById(id);
        return Response.ok(booking).build();
    }

    @POST
    public Response create(@Valid BookingDTO dto) {

        Booking booking = new Booking(
                dto.vehicleId(),
                dto.customerName(),
                dto.startDate(),
                dto.endDate()
        );

        bookingService.createBook(booking);

        return Response.status(Response.Status.CREATED).entity(dto).build();
    }

    public record BookingDTO(
            @NotNull
            @Positive
            Long vehicleId,
            @NotBlank
            String customerName,
            @FutureOrPresent
            LocalDate startDate,
            @FutureOrPresent
            LocalDate endDate
    ) {
    }
}

