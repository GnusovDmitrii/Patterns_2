package ru.netology;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .build();

    private DataGenerator() {}

    public static String generateLogin() {
        return "user_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static String generatePassword() {
        return "pass_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static RegistrationDto registerUser(String login, String password, String status) {
        RegistrationDto user = new RegistrationDto(login, password, status);
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
        return user;
    }

    public static RegistrationDto createActiveUser() {
        return registerUser(generateLogin(), generatePassword(), "active");
    }

    public static RegistrationDto createBlockedUser() {
        return registerUser(generateLogin(), generatePassword(), "blocked");
    }

    public static RegistrationDto createUser(String login, String password, String status) {
        return registerUser(login, password, status);
    }
}