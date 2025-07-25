package dev.matheuscruz.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;

@QuarkusTest
class BookingResourceTest {

    @Test
    void deveCriarUmAgendamento() {

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "startDate": "2025-08-01",
                          "endDate": "2025-08-02",
                          "customerName": "Everton",
                          "vehicleId": 1
                        }
                        """)
                .post("/bookings")
                .then()
                .statusCode(201);
    }

    @Test
    void naoDeveCriarUmAgendamentoParaUmaDataDeInicioNoPassado() {

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "startDate": "2024-08-01",
                          "endDate": "2025-08-02",
                          "customerName": "Everton",
                          "vehicleId": 1
                        }
                        """)
                .post("/bookings")
                .then()
                .statusCode(400);
    }

    @Test
    void naoDeveCriarUmAgendamentoComADataDeFimMenorDoQueADataDeInicio() {

        LocalDate startDate = LocalDate.now().plusDays(10);
        LocalDate endDate = startDate.minusDays(1);

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "startDate": "%s",
                          "endDate": "%s",
                          "customerName": "Everton",
                          "vehicleId": 1
                        }
                        """.formatted(startDate, endDate))
                .post("/bookings")
                .then()
                .body("message", Matchers.containsString("End date cannot be before start date"))
                .statusCode(400);
    }
}