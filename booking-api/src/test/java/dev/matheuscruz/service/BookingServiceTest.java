package dev.matheuscruz.service;

import dev.matheuscruz.client.VehicleApiClient;
import dev.matheuscruz.model.Booking;
import dev.matheuscruz.repository.BookingDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.concurrent.TimeoutException;

class BookingServiceTest {


    @Test
    void naoDeveCriarUmAgendamentoQuandoNaoExistirUmVeiculo() {

        BookingDAO bookingDAO = Mockito.mock(BookingDAO.class);
        VehicleApiClient vehicleApiClient = Mockito.mock(VehicleApiClient.class);

        Mockito.when(vehicleApiClient.findVehicleById(1L)).thenReturn(null);

        BookingService bookingService = new BookingService(bookingDAO, vehicleApiClient);


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

        BookingDAO bookingDAO = Mockito.mock(BookingDAO.class);
        VehicleApiClient vehicleApiClient = Mockito.mock(VehicleApiClient.class);

        Mockito.when(vehicleApiClient.findVehicleById(1L)).thenReturn(new VehicleApiClient.Vehicle(
                "RENTED"
        ));

        BookingService bookingService = new BookingService(bookingDAO, vehicleApiClient);

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

        BookingDAO bookingDAO = Mockito.mock(BookingDAO.class);
        VehicleApiClient vehicleApiClient = Mockito.mock(VehicleApiClient.class);

        Mockito.when(vehicleApiClient.findVehicleById(Mockito.anyLong())).thenReturn(new VehicleApiClient.Vehicle(
                "AVAILABLE"
        ));

        BookingService bookingService = new BookingService(bookingDAO, vehicleApiClient);

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

        BookingDAO bookingDAO = Mockito.mock(BookingDAO.class);
        VehicleApiClient vehicleApiClient = Mockito.mock(VehicleApiClient.class);

        Mockito.when(vehicleApiClient.findVehicleById(Mockito.anyLong()))
                .thenThrow(new TimeoutException());

        BookingService bookingService = new BookingService(bookingDAO, vehicleApiClient);

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