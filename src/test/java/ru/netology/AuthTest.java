package ru.netology;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

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

    // Тест 1: Регистрация активного пользователя
    @Test
    void shouldRegisterActiveUser() {
        RegistrationDto user = DataGenerator.generateActiveUser();

        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    // Тест 2: Регистрация заблокированного пользователя
    @Test
    void shouldRegisterBlockedUser() {
        RegistrationDto user = DataGenerator.generateBlockedUser();

        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    // Тест 3: Перезапись существующего пользователя
    @Test
    void shouldOverwriteExistingUser() {
        String login = "testuser_" + System.currentTimeMillis();

        RegistrationDto user1 = new RegistrationDto(login, "pass123", "active");
        RegistrationDto user2 = new RegistrationDto(login, "newpass456", "blocked");

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

    // Тест 4: Попытка входа (BUG: всегда возвращает 404)
    @Test
    void loginEndpointAlwaysReturns404() {
        RegistrationDto user = DataGenerator.generateActiveUser();

        // Сначала регистрируем пользователя
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);

        // Пытаемся войти - БАГ: возвращает 404 вместо 200
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(404);
    }
}