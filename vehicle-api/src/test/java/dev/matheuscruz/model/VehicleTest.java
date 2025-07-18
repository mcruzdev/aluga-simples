package dev.matheuscruz.model;

import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VehicleTest {

    @Test
    void shouldCreateVehicle() {
        assertDoesNotThrow(() -> new Vehicle("Mobi", 2025, "1.0", "Fiat"));
    }

    @Test
    void shouldCreateVehicleWithValidState() {
        Vehicle vehicle = new Vehicle("Mobi", 2025, "1.0", "Fiat");
        assertEquals("Mobi", vehicle.getModel());
        assertEquals(2025, vehicle.getYear());
        assertEquals("1.0", vehicle.getEngine());
        assertEquals("Fiat", vehicle.getBrand());
        assertEquals(VehicleStatus.AVAILABLE, vehicle.getStatus());
    }

    @Test
    void shouldNotCreateVehicleWithNullBrand() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Vehicle("Mobi", 2025, "1.0", null)
        );
        assertEquals("brand cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldNotCreateVehicleWithEmptyBrand() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Vehicle("Mobi", 2025, "1.0", "")
        );
        assertEquals("brand cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldNotCreateVehicleWithBlankModel() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Vehicle("   ", 2025, "1.0", "Fiat")
        );
        assertEquals("model must not be null", exception.getMessage());
    }

    @Test
    void shouldNotCreateVehicleWithInvalidYear() {
        int invalidYear = Year.now().getValue() - 4;
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Vehicle("Mobi", invalidYear, "1.0", "Fiat")
        );
        assertTrue(exception.getMessage().startsWith("year must be between"));
    }

    @Test
    void shouldNotCreateVehicleWithFutureYear() {
        int futureYear = Year.now().getValue() + 1;
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Vehicle("Mobi", futureYear, "1.0", "Fiat")
        );
        assertTrue(exception.getMessage().startsWith("year must be between"));
    }

    @Test
    void shouldChangeStatusFromAvailableToRented() {
        Vehicle vehicle = new Vehicle("Mobi", 2025, "1.0", "Fiat");
        vehicle.setStatus(VehicleStatus.RENTED);
        assertEquals(VehicleStatus.RENTED, vehicle.getStatus());
    }

    @Test
    void shouldChangeStatusFromRentedToUnderMaintenance() {
        Vehicle vehicle = new Vehicle("Mobi", 2025, "1.0", "Fiat");
        vehicle.setStatus(VehicleStatus.RENTED);
        vehicle.setStatus(VehicleStatus.UNDER_MAINTENANCE);
        assertEquals(VehicleStatus.UNDER_MAINTENANCE, vehicle.getStatus());
    }

    @Test
    void shouldChangeStatusFromUnderMaintenanceToAvailable() {
        Vehicle vehicle = new Vehicle("Mobi", 2025, "1.0", "Fiat");
        vehicle.setStatus(VehicleStatus.UNDER_MAINTENANCE);
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        assertEquals(VehicleStatus.AVAILABLE, vehicle.getStatus());
    }

    @Test
    void shouldIgnoreSameStatusTransition() {
        Vehicle vehicle = new Vehicle("Mobi", 2025, "1.0", "Fiat");
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        assertEquals(VehicleStatus.AVAILABLE, vehicle.getStatus());
    }

    @Test
    void shouldThrowExceptionForInvalidTransition() {
        Vehicle vehicle = new Vehicle("Mobi", 2025, "1.0", "Fiat");
        vehicle.setStatus(VehicleStatus.UNDER_MAINTENANCE);

        Exception exception = assertThrows(IllegalStateException.class, () ->
                vehicle.setStatus(VehicleStatus.RENTED)
        );
        assertTrue(exception.getMessage().contains("Status transition from"));
    }
}
