package dev.matheuscruz.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "vehicle")
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

    @OneToMany
    @JoinColumn(name = "vehicle_id")
    private final List<Maintenance> maintenances = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "vehicle_accesory",
            joinColumns = @JoinColumn(name = "vehicle_id"),
            inverseJoinColumns = @JoinColumn(name = "accessory_id"))
    private final Set<Accessory> accessories = new HashSet<>();

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

    public List<Maintenance> getMaintenances() {
        return maintenances;
    }

    public void changeStatus(VehicleStatus vehicleStatus) {
        Set<VehicleStatus> possibleStatus = VEHICLE_STATE_MACHINE.get(this.status);

        if (vehicleStatus.equals(this.status)) {
            return;
        }

        if (possibleStatus.contains(vehicleStatus)) {
            this.status = vehicleStatus;
        } else {
            throw new IllegalStateException("Status transition from '" + this.status +
                    "' to '" + vehicleStatus + "' is not allowed. Allowed: " + possibleStatus);
        }
    }

    public void moveForMaintenance(Maintenance maintenance) {
        this.changeStatus(VehicleStatus.UNDER_MAINTENANCE);
        this.maintenances.add(maintenance);
    }

    public void addAccessory(Accessory accessory) {
        this.accessories.add(accessory);
        accessory.addVehicle(this);
    }

    public Set<Accessory> getAccessories() {
        return this.accessories;
    }
}