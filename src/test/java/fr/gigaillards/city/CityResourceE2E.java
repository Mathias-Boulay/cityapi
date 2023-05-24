package fr.gigaillards.city;

import fr.gigaillards.entity.City;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class CityResourceE2E {
    @Test
    public void testFindAllEndpoint() {
        given()
                .when().get("/city")
                .then()
                .statusCode(200)
                .log();
    }

    @Test
    public void testCreateOneEndpoint() {
        City newCity = new City();
        newCity.setLon(12f);
        newCity.setLat(8f);
        newCity.setName("Aubagne");
        newCity.setZipCode("13400");
        newCity.setDepartmentCode("13");

        given()
                .when()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .with()
                .body(newCity)
                .post("/city")
                .then()
                .statusCode(201)
                .body();
    }
}
