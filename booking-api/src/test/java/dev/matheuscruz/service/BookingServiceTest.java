package dev.matheuscruz.service;

import dev.matheuscruz.repository.VehicleRepository;
import dev.matheuscruz.model.Booking;
import dev.matheuscruz.repository.BookingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.concurrent.TimeoutException;

class BookingServiceTest {

    @Test
    void naoDeveCriarUmAgendamentoQuandoNaoExistirUmVeiculo() {

        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        VehicleRepository vehicleRepository = Mockito.mock(VehicleRepository.class);

        Mockito.when(vehicleRepository.findVehicleById(1L)).thenReturn(null);

        BookingService bookingService = new BookingService(bookingRepository, vehicleRepository);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            bookingService.createBook(new Booking(
                    1L,
                    "Daniel",
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(2)
            ));
        });

    }

    @Test
    void naoDeveCriarQuandoOStatusForDiferenteDeAvailable() {

        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        VehicleRepository vehicleRepository = Mockito.mock(VehicleRepository.class);

        Mockito.when(vehicleRepository.findVehicleById(1L)).thenReturn(new VehicleRepository.Vehicle(
                "RENTED"
        ));

        BookingService bookingService = new BookingService(bookingRepository, vehicleRepository);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            bookingService.createBook(new Booking(
                    1L,
                    "Daniel",
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(2)
            ));
        });

    }

    @Test
    void deveCriarUmAgendamentoQuandoTudoEstiverOk() {

        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        VehicleRepository vehicleRepository = Mockito.mock(VehicleRepository.class);

        Mockito.when(vehicleRepository.findVehicleById(Mockito.anyLong())).thenReturn(new VehicleRepository.Vehicle(
                "AVAILABLE"
        ));

        BookingService bookingService = new BookingService(bookingRepository, vehicleRepository);

        Assertions.assertDoesNotThrow(() -> {
            bookingService.createBook(new Booking(
                    1L,
                    "Daniel",
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(2)
            ));
        });
    }

    @Test
    void deveTratarAFalhaNaComunicacoComOVehicleApiQuandoReceberTimeout() {

        BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
        VehicleRepository vehicleRepository = Mockito.mock(VehicleRepository.class);

        Mockito.when(vehicleRepository.findVehicleById(Mockito.anyLong()))
                .thenThrow(new TimeoutException());

        BookingService bookingService = new BookingService(bookingRepository, vehicleRepository);

        Assertions.assertDoesNotThrow(() -> {
            bookingService.createBook(new Booking(
                    1L,
                    "Daniel",
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(2)
            ));
        });
    }
}