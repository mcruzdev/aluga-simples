package dev.matheuscruz.model;

import dev.matheuscruz.exception.InvalidBookingDateException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "vehicle_id")
    Long vehicleId;

    @Column(name = "customer_name")
    String customerName;

    @Column(name = "start_date")
    LocalDate startDate;

    @Column(name = "end_date")
    LocalDate endDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    BookingStatus status;

    protected Booking() {
    }

    public Booking(Long vehicleId, String customerName, LocalDate startDate, LocalDate endDate) {
        this.status = BookingStatus.CREATED;
        this.vehicleId = vehicleId;
        this.customerName = customerName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.validate();
    }

    private void validate() {
        if (endDate.isBefore(startDate)) {
            throw new InvalidBookingDateException("End date cannot be before start date");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public BookingStatus getStatus() {
        return status;
    }
}
