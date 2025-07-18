package dev.matheuscruz.dto;

import dev.matheuscruz.model.Accessory;
import dev.matheuscruz.model.Vehicle;
import dev.matheuscruz.model.VehicleStatus;

import java.util.Set;
import java.util.stream.Collectors;

public record VehicleResponse(Long id, String brand, String model, int year, String engine, VehicleStatus status,
                              String carTitle, Set<String> accessories) {

    public VehicleResponse(Vehicle vehicle) {
        this(vehicle.getId(), vehicle.getBrand(), vehicle.getModel(), vehicle.getYear(), vehicle.getEngine(), vehicle.getStatus(),
                "%s %s %s".formatted(vehicle.getBrand(), vehicle.getModel(), vehicle.getEngine()),
                vehicle.getAccessories().stream().map(Accessory::getName).collect(Collectors.toSet()));
    }
}
