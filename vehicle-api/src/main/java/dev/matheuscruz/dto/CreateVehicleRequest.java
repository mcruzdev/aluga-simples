package dev.matheuscruz.dto;

public record CreateVehicleRequest(String brand, String model, Integer year, String engine) {
}
