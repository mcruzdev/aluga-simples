package dev.matheuscruz.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
public class Vehicle extends PanacheEntityBase {

    private static final Map<VehicleStatus, Set<VehicleStatus>> VEHICLE_STATE_MACHINE = new HashMap<>() {
    };

    static {
        VEHICLE_STATE_MACHINE.put(VehicleStatus.AVAILABLE, Set.of(VehicleStatus.RENTED, VehicleStatus.UNDER_MAINTENANCE));
        VEHICLE_STATE_MACHINE.put(VehicleStatus.RENTED, Set.of(VehicleStatus.AVAILABLE, VehicleStatus.UNDER_MAINTENANCE));
        VEHICLE_STATE_MACHINE.put(VehicleStatus.UNDER_MAINTENANCE, Set.of(VehicleStatus.AVAILABLE));
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String model;
    private String brand;
    @Enumerated(EnumType.STRING)
    private VehicleStatus status;
    @Column(name = "vehicle_year")
    private int year;
    private String engine;

    protected Vehicle() {
    }

    public Vehicle(String model, int year, String engine, String brand) {

        this.status = VehicleStatus.AVAILABLE;
        this.model = model;
        this.year = year;
        this.engine = engine;
        this.brand = brand;

        validate();
    }

    private void validate() {

        if (brand == null || brand.isEmpty()) {
            throw new IllegalArgumentException("brand cannot be null or empty");
        }

        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("model must not be null");
        }

        if (engine == null || engine.isBlank()) {
            throw new IllegalArgumentException("engine must not be null");
        }

        int currentYear = Year.now().getValue();
        int limit = currentYear - 3;
        if (year < limit || year > currentYear) {
            throw new IllegalArgumentException("year must be between %d and %d".formatted(
                    limit, currentYear
            ));
        }
    }

    public Long getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public int getYear() {
        return year;
    }

    public String getEngine() {
        return engine;
    }

    public String getBrand() {
        return brand;
    }

    public boolean isRented() {
        return this.getStatus().equals(VehicleStatus.RENTED);
    }

    public void setStatus(VehicleStatus incomingStatus) {
        Set<VehicleStatus> possibleStatus = VEHICLE_STATE_MACHINE.get(this.status);

        if (incomingStatus.equals(this.status)) {
            return;
        }

        if (possibleStatus.contains(incomingStatus)) {
            this.status = incomingStatus;
        } else {
            throw new IllegalStateException("Status transition from '" + this.status +
                    "' to '" + incomingStatus + "' is not allowed. Allowed: " + possibleStatus);
        }
    }
}