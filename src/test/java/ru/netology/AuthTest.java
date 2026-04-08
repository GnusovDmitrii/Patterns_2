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

    // Тест 1: Успешное создание активного пользователя
    @Test
    void shouldCreateActiveUser() {
        RegistrationDto user = DataGenerator.generateActiveUser();

        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    // Тест 2: Успешное создание заблокированного пользователя
    @Test
    void shouldCreateBlockedUser() {
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

        // Первое создание
        given()
                .spec(requestSpec)
                .body(user1)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);

        // Перезапись (должна быть успешной)
        given()
                .spec(requestSpec)
                .body(user2)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    // Тест 4: Создание нескольких пользователей
    @Test
    void shouldCreateMultipleUsers() {
        for (int i = 0; i < 5; i++) {
            RegistrationDto user = DataGenerator.generateActiveUser();
            given()
                    .spec(requestSpec)
                    .body(user)
                    .when()
                    .post("/api/system/users")
                    .then()
                    .statusCode(200);
        }
    }

    // Тест 5: Создание пользователя с пустым логином (ожидаем 200 - баг приложения)
    @Test
    void shouldCreateUserWithEmptyLogin() {
        RegistrationDto user = new RegistrationDto("", "password123", "active");

        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200); // Фактическое поведение приложения
    }

    // Тест 6: Создание пользователя с пустым паролем (ожидаем 200 - баг приложения)
    @Test
    void shouldCreateUserWithEmptyPassword() {
        RegistrationDto user = new RegistrationDto("validuser_" + System.currentTimeMillis(), "", "active");

        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200); // Фактическое поведение приложения
    }

    // Тест 7: Создание пользователя с невалидным статусом (ожидаем 500 - баг приложения)
    @Test
    void shouldReturn500ForInvalidStatus() {
        RegistrationDto user = new RegistrationDto("testuser", "pass123", "invalid_status");

        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(500); // Фактическое поведение приложения
    }
}