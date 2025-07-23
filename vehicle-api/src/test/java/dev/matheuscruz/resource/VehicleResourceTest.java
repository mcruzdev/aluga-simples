package dev.matheuscruz.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

@QuarkusTest
class VehicleResourceTest {

    @Test
    void shouldReturn201WhenSendAValidRequest() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("""
                         {
                           "brand": "Fiat",
                           "model": "Mobi",
                           "status": "AVAILABLE",
                           "year": 2022,
                           "engine": "1.0"
                         }
                        """)
                .post("/api/v1/vehicles")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("id", Matchers.notNullValue())
                .header("Location", Matchers.containsString("/api/v1/vehicles/"));
    }

    @Test
    void shouldGetAllVehicles() {
        RestAssured.given()
                .get("/api/v1/vehicles")
                .then()
                .body("size()", Matchers.not(Matchers.empty()))
                .statusCode(HttpStatus.SC_OK)
                .body("[0].id", Matchers.notNullValue())
                .body("[0].carTitle", Matchers.equalTo("Honda City 1.5"))
                .log();
    }

    @Test
    void shouldGetVehicleByID() {
        RestAssured.given()
                .get("/api/v1/vehicles/1")
                .then()
                .body("id", Matchers.equalTo(1))
                .body("carTitle", Matchers.equalTo("Honda City 1.5"))
                .body("status", Matchers.equalTo("AVAILABLE"));
    }

    @Test
    void shouldReturn404WhenThereIsNoVehicleWithProvidedID() {
        RestAssured.given()
                .get("/api/v1/vehicles/1000")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldUpdateStatusPartially() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("""
                        { "status": "RENTED" }
                        """)
                .patch("/api/v1/vehicles/1")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("status", Matchers.equalTo("RENTED"));
    }

    @Test
    void shouldReturn404WhenUpdatingANonExistentVehicle() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("""
                        { "status": "RENTED" }
                        """)
                .patch("/api/v1/vehicles/1000")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void shouldReturn400WhenSendingInvalidYear() {
        int invalidYear = java.time.Year.now().getValue() - 4;

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("""
                         {
                           "brand": "Fiat",
                           "model": "Uno",
                           "status": "AVAILABLE",
                           "year": %d,
                           "engine": "1.0"
                         }
                        """.formatted(invalidYear))
                .post("/api/v1/vehicles")
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR); // exception gerada na validação
    }

    @Test
    void shouldReturn500WhenCreatingWithMissingRequiredFields() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "model": "",
                          "brand": "",
                          "year": 2025,
                          "engine": ""
                        }
                        """)
                .post("/api/v1/vehicles")
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    void shouldReturn400WhenSendingInvalidStatusTransition() {
        int id = RestAssured.given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "brand": "Fiat",
                          "model": "Pulse",
                          "year": 2025,
                          "engine": "1.0"
                        }
                        """)
                .post("/api/v1/vehicles")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .jsonPath()
                .getInt("id");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("""
                        { "status": "UNDER_MAINTENANCE" }
                        """)
                .patch("/api/v1/vehicles/%d".formatted(id))
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("status", Matchers.equalTo("UNDER_MAINTENANCE"));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("""
                        { "status": "RENTED" }
                        """)
                .patch("/api/v1/vehicles/%d".formatted(id))
                .then()
                .statusCode(HttpStatus.SC_CONFLICT);
    }

    @Test
    void shouldReturn500WhenSendingNullStatusInPatch() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("""
                        { "status": null }
                        """)
                .patch("/api/v1/vehicles/1")
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    void shouldReturnCreatedVehicleFieldsCorrectly() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("""
                         {
                           "brand": "Chevrolet",
                           "model": "Onix",
                           "status": "AVAILABLE",
                           "year": 2023,
                           "engine": "1.0"
                         }
                        """)
                .post("/api/v1/vehicles")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("model", Matchers.equalTo("Onix"))
                .body("brand", Matchers.equalTo("Chevrolet"))
                .body("year", Matchers.equalTo(2023))
                .body("engine", Matchers.equalTo("1.0"))
                .body("status", Matchers.equalTo("AVAILABLE"));
    }

    @Test
    void shouldCreateMultipleVehiclesAndReturnList() {
        for (int i = 0; i < 3; i++) {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body("""
                            {
                              "brand": "Toyota",
                              "model": "Yaris",
                              "year": 2023,
                              "engine": "1.5"
                            }
                            """)
                    .post("/api/v1/vehicles")
                    .then()
                    .statusCode(HttpStatus.SC_CREATED);
        }

        RestAssured.given()
                .get("/api/v1/vehicles")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("findAll { it.carTitle.contains('Yaris') }.size()", Matchers.greaterThanOrEqualTo(3));
    }


    @Test
    void shouldAddMaintenanceToVehicle() {
        int vehicleId = RestAssured.given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "brand": "Volkswagen",
                          "model": "Gol",
                          "year": 2025,
                          "engine": "1.6"
                        }
                        """)
                .post("/api/v1/vehicles")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .jsonPath()
                .getInt("id");

        String location = RestAssured.given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "problem": "Motor não liga"
                        }
                        """)
                .post("/api/v1/vehicles/%d/maintenances".formatted(vehicleId))
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .header("Location", Matchers.containsString("/api/v1/vehicles/" + vehicleId + "/maintenances/"))
                .extract()
                .header("Location");

        RestAssured.given()
                .get(location)
                .then()
                .body("id", Matchers.equalTo(1));
    }

}