package dev.matheuscruz.resource;

import dev.matheuscruz.repository.BookingRepository;
import dev.matheuscruz.repository.VehicleRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

@QuarkusTest
class BookingResourceTest {

    long vehicleAvailable = 1L;

    @InjectMock
    @RestClient
    VehicleRepository vehicleRepository;

    @Inject
    BookingRepository bookingRepository;

    @Transactional
    @BeforeEach
    void cleanUp() {
        bookingRepository.deleteAll();
    }

    @Test
    void shouldCreateABooking() {

        Mockito.when(vehicleRepository.findVehicleById(vehicleAvailable))
                        .thenReturn(new VehicleRepository.Vehicle("AVAILABLE"));

        LocalDate startDate = LocalDate.now().plusDays(1);

        LocalDate endDate = startDate.plusDays(10);

        BookingResource.BookingDTO body = new BookingResource.BookingDTO(
                vehicleAvailable, "Matheus Cruz", startDate, endDate
        );

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .post("/bookings")
                .then()
                .statusCode(201);
    }

    @Test
    void shouldNotCreateBookingWithStartDateInThePast() {

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "startDate": "2024-08-01",
                          "endDate": "2025-08-02",
                          "customerName": "John",
                          "vehicleId": 1
                        }
                        """)
                .post("/bookings")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldNotCreateBookingWithEndDateBeforeStartDate() {

        LocalDate startDate = LocalDate.now().plusDays(10);

        LocalDate endDate = startDate.minusDays(1);

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "startDate": "%s",
                          "endDate": "%s",
                          "customerName": "John",
                          "vehicleId": 1
                        }
                        """.formatted(startDate, endDate))
                .post("/bookings")
                .then()
                .body("message", Matchers.containsString("End date cannot be before start date"))
                .statusCode(400);
    }

    @Test
    void shouldReturnConflictWhenStartDateIsConflicting() {

        Mockito.when(vehicleRepository.findVehicleById(vehicleAvailable))
                .thenReturn(new VehicleRepository.Vehicle("AVAILABLE"));

        LocalDate startDate = LocalDate.now().plusDays(10);
        LocalDate endDate = startDate.plusDays(1);
        BookingResource.BookingDTO johnBooking = new BookingResource.BookingDTO(
                vehicleAvailable, "John", startDate, endDate
        );

        given()
                .contentType(ContentType.JSON)
                .body(johnBooking)
                .post("/bookings")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());

        BookingResource.BookingDTO eliotBooking = new BookingResource.BookingDTO(
                vehicleAvailable, "Eliot", startDate, endDate.plusDays(1)
        );

        given()
                .contentType(ContentType.JSON)
                .body(eliotBooking)
                .post("/bookings")
                .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode());
    }

    @Test
    void shouldReturnConflictWhenEndDateIsConflicting() {

        Mockito.when(vehicleRepository.findVehicleById(vehicleAvailable))
                .thenReturn(new VehicleRepository.Vehicle("AVAILABLE"));

        LocalDate startDate = LocalDate.now().plusDays(10);
        LocalDate endDate = startDate.plusDays(1);
        BookingResource.BookingDTO johnBooking = new BookingResource.BookingDTO(
                vehicleAvailable, "John", startDate, endDate
        );

        given()
                .contentType(ContentType.JSON)
                .body(johnBooking)
                .post("/bookings")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());

        BookingResource.BookingDTO eliotBooking = new BookingResource.BookingDTO(
                vehicleAvailable, "Eliot", startDate.minusDays(1), endDate
        );

        given()
                .contentType(ContentType.JSON)
                .body(eliotBooking)
                .post("/bookings")
                .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode());
    }

    @Test
    void shouldReturnConflict() {
        Mockito.when(vehicleRepository.findVehicleById(vehicleAvailable))
                .thenReturn(new VehicleRepository.Vehicle("AVAILABLE"));

        LocalDate startDate = LocalDate.now().plusDays(10);
        LocalDate endDate = startDate.plusDays(1);
        BookingResource.BookingDTO johnBooking = new BookingResource.BookingDTO(
                vehicleAvailable, "John", startDate, endDate
        );

        given()
                .contentType(ContentType.JSON)
                .body(johnBooking)
                .post("/bookings")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());

        BookingResource.BookingDTO eliotBooking = new BookingResource.BookingDTO(
                vehicleAvailable, "Eliot", startDate, endDate
        );

        given()
                        .contentType(ContentType.JSON)
                        .body(eliotBooking)
                        .post("/bookings")
                        .then()
                        .statusCode(Response.Status.CONFLICT.getStatusCode());
    }

    @Test
    void shouldAllowOnlyOneBookingWhenPostingConcurrentlyForSameVehicleAndWindow() throws Exception {

        // Define a booking window: today until 5 days from now
        LocalDate now = LocalDate.now();
        LocalDate future = now.plusDays(5);

        // Request body used by both concurrent requests (same vehicle and same window)
        BookingResource.BookingDTO body = new BookingResource.BookingDTO(
                1L, "John", now, future
        );

        // We will fire two requests at (almost) the same time
        int threads = 2;

        // Fixed-size pool to run both tasks in parallel
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        // Latch #1: counts how many worker threads are ready to start
        CountDownLatch ready = new CountDownLatch(threads);

        // Latch #2: the “starter pistol”; released once to let all ready threads proceed together
        CountDownLatch start = new CountDownLatch(1);

        // Latch #3: tracks when all worker threads have finished
        CountDownLatch done = new CountDownLatch(threads);

        // Thread-safe collection to store HTTP status codes returned by each request
        List<Integer> statusCodes = Collections.synchronizedList(new ArrayList<>());

        // The task each thread will run: wait for the common start signal, then POST /bookings
        Runnable task = () -> {
            try {
                // Signal this thread is ready and waiting
                ready.countDown();

                // Wait (with timeout) for the test to release the simultaneous start
                start.await(3, TimeUnit.SECONDS);

                // Perform the HTTP request to create the booking
                int code =
                        given()
                                .contentType(ContentType.JSON)
                                .body(body)
                                .post("/bookings")
                                .then()
                                .extract()
                                .statusCode();

                // Record the outcome of this thread
                statusCodes.add(code);
            } catch (Exception e) {
                // Mark unexpected failures with a sentinel value
                statusCodes.add(-1);
            } finally {
                // Always signal completion, even if an exception occurred
                done.countDown();
            }
        };

        // Submit both tasks to the pool
        for (int i = 0; i < threads; i++) {
            pool.submit(task);
        }

        // Wait (briefly) until both threads report they’re ready to start together
        ready.await(3, TimeUnit.SECONDS);

        // Release the simultaneous start so both requests race to the server
        start.countDown();

        // Wait for both requests to complete (or time out)
        done.await(5, TimeUnit.SECONDS);

        // Cleanly shut down the executor
        pool.shutdown();

        // Sanity check: we must have exactly two results (one per thread)
        Assertions.assertEquals(2, statusCodes.size());

        // Count the number of Created (201) and Conflict (409) responses
        long created = statusCodes.stream().filter(c -> c == 201).count();
        long conflicted = statusCodes.stream().filter(c -> c == 409).count();

        // Expectation of correct concurrency control:
        // exactly one booking is created and the other is rejected due to overlap
        Assertions.assertEquals(1, created, "must have at least once status created");
        Assertions.assertEquals(1, conflicted, "must have at least once status conflict");
    }

}
