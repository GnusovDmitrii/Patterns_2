package ru.netology;

import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

public class AuthTest {

    private static RequestSpecification requestSpec;

    @BeforeAll
    static void setUpAll() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(9999)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    void shouldRegisterActiveUser() {
        RegistrationDto user = DataGenerator.createActiveUser();

        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldRegisterBlockedUser() {
        RegistrationDto user = DataGenerator.createBlockedUser();

        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldOverwriteExistingUser() {
        String login = "testuser_" + System.currentTimeMillis();

        RegistrationDto user1 = DataGenerator.createUser(login, "pass123", "active");
        RegistrationDto user2 = DataGenerator.createUser(login, "newpass456", "blocked");

        given()
                .spec(requestSpec)
                .body(user1)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);

        given()
                .spec(requestSpec)
                .body(user2)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }
}