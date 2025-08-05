package dev.matheuscruz.resource.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record ProblemDTO(String message,
                         String occurredAt) {

    public ProblemDTO(String message) {
        this(message, localDate());
    }

    static String localDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return now.format(formatter);
    }
}
