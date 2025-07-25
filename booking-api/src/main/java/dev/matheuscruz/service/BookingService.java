package dev.matheuscruz.service;

import dev.matheuscruz.client.VehicleApiClient;
import dev.matheuscruz.model.Booking;
import dev.matheuscruz.repository.BookingDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class BookingService {

    private BookingDAO bookingDAO;
    private VehicleApiClient vehicleApiClient;

    public BookingService(BookingDAO bookingDAO,
                          @RestClient VehicleApiClient vehicleApiClient) {
        this.bookingDAO = bookingDAO;
        this.vehicleApiClient = vehicleApiClient;
    }

    @Transactional
    public void createBook(Booking booking) {

        VehicleApiClient.Vehicle vehicle = vehicleApiClient.findVehicleById(booking.getVehicleId());

        if (vehicle == null) {
            throw new IllegalStateException("Vehicle does not exist");
        }

        if (!vehicle.status().equals("AVAILABLE")) {
            throw new IllegalStateException("Vehicle is not available");
        }

        this.bookingDAO.persist(booking);
    }

    public Booking getBookById(Long id) {
        return this.bookingDAO.findById(id, LockModeType.PESSIMISTIC_WRITE);
    }
}