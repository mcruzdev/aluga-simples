package dev.matheuscruz.exception;

public class BookingWithConflictException extends RuntimeException {
    public BookingWithConflictException(String message) {
        super(message);
    }
}
