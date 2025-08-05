package dev.matheuscruz.service;

import dev.matheuscruz.exception.BookingWithConflictException;
import dev.matheuscruz.exception.VehicleNotAvailableException;
import dev.matheuscruz.model.Booking;
import dev.matheuscruz.repository.BookingRepository;
import dev.matheuscruz.repository.VehicleRepository;
import dev.matheuscruz.resource.BookingResource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Optional;

@ApplicationScoped
public class BookingService {

    private final BookingRepository bookingRepository;
    private final VehicleRepository vehicleRepository;

    public BookingService(BookingRepository bookingRepository,
                          @RestClient VehicleRepository vehicleRepository) {
        this.bookingRepository = bookingRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public Booking createBook(Booking booking) {

        Optional<Booking> overlappingBooking = this.bookingRepository
                .findOverlappingBooking(booking.getVehicleId(), booking.getStartDate(), booking.getEndDate());

        if (overlappingBooking.isPresent()) {
            throw new BookingWithConflictException("Vehicle is already booked");
        }

        VehicleRepository.Vehicle vehicle = vehicleRepository.findVehicleById(booking.getVehicleId());

        if (!vehicle.status().equals("AVAILABLE")) {
            throw new VehicleNotAvailableException("Vehicle is not available");
        }

        this.bookingRepository.persist(booking);

        return booking;
    }

    public Booking getBookById(Long id) {
        return this.bookingRepository.findById(id, LockModeType.PESSIMISTIC_WRITE);
    }
}