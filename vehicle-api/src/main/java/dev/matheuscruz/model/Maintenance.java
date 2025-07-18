package dev.matheuscruz.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "maintenance")
public class Maintenance extends PanacheEntityBase {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "created_at")
    Instant createdAt;
    @Column(name = "reason")
    String reason;
    @Column(name = "vehicle_id")
    Long vehicleId;

    protected Maintenance() {
    }

    public Maintenance(String reason, Long vehicleId) {
        this.createdAt = Instant.now();
        this.reason = reason;
        this.vehicleId = vehicleId;
    }

    public Long getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getReason() {
        return reason;
    }

    public Long getVehicleId() {
        return vehicleId;
    }
}
