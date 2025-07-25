package dev.matheuscruz.repository;

import dev.matheuscruz.model.Booking;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

// Data Access Object
@ApplicationScoped
// Quarkus adiciona esse bean na sacola, ou na bolsa de beans que você injeta via construtor
public class BookingDAO implements PanacheRepository<Booking> {
}
