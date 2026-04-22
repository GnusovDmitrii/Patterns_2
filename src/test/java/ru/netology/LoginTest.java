package ru.netology;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class LoginTest {

    private static final String BASE_URL = "http://localhost:9999";

    @BeforeAll
    static void setUpAll() {
    }

    @BeforeEach
    void setUp() {
        open(BASE_URL);
    }

    // Тест 1: Вход активного зарегистрированного пользователя
    @Test
    void shouldLoginWithActiveRegisteredUser() {
        RegistrationDto user = DataGenerator.createActiveUser();

        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $("[data-test-id='action-login']").click();

        // Проверяем, что появилась страница личного кабинета
        $("[data-test-id='dashboard']").shouldBe(visible);
        $("[data-test-id='dashboard']").shouldHave(text("Личный кабинет"));
    }

    // Тест 2: Вход заблокированного пользователя
    @Test
    void shouldNotLoginWithBlockedRegisteredUser() {
        RegistrationDto user = DataGenerator.createBlockedUser();

        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $("[data-test-id='action-login']").click();

        // Проверяем, что появилось сообщение об ошибке
        $("[data-test-id='error-notification']").shouldBe(visible);
        $("[data-test-id='error-notification']").shouldHave(text("Пользователь заблокирован"));
    }

    // Тест 3: Вход незарегистрированного пользователя
    @Test
    void shouldNotLoginWithUnregisteredActiveUser() {
        String login = DataGenerator.generateLogin();
        String password = DataGenerator.generatePassword();

        $("[data-test-id='login'] input").setValue(login);
        $("[data-test-id='password'] input").setValue(password);
        $("[data-test-id='action-login']").click();

        // Проверяем, что появилось сообщение об ошибке
        $("[data-test-id='error-notification']").shouldBe(visible);
        $("[data-test-id='error-notification']").shouldHave(text("Неверный логин или пароль"));
    }

    // Тест 4: Вход с неправильным паролем
    @Test
    void shouldNotLoginWithValidUserWrongPassword() {
        RegistrationDto user = DataGenerator.createActiveUser();

        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue("wrong_password");
        $("[data-test-id='action-login']").click();

        // Проверяем, что появилось сообщение об ошибке
        $("[data-test-id='error-notification']").shouldBe(visible);
        $("[data-test-id='error-notification']").shouldHave(text("Неверный логин или пароль"));
    }

    // Тест 5: Вход с неправильным логином
    @Test
    void shouldNotLoginWithValidUserWrongLogin() {
        RegistrationDto user = DataGenerator.createActiveUser();

        $("[data-test-id='login'] input").setValue("wrong_" + user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $("[data-test-id='action-login']").click();

        // Проверяем, что появилось сообщение об ошибке
        $("[data-test-id='error-notification']").shouldBe(visible);
        $("[data-test-id='error-notification']").shouldHave(text("Неверный логин или пароль"));
    }

    // Тест 6: Пустой логин
    @Test
    void shouldNotLoginWithEmptyLogin() {
        RegistrationDto user = DataGenerator.createActiveUser();

        $("[data-test-id='login'] input").setValue("");
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $("[data-test-id='action-login']").click();

        // Проверяем, что появилось сообщение об ошибке валидации
        $("[data-test-id='login'].input_invalid").shouldBe(visible);
        $("[data-test-id='login'] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    // Тест 7: Пустой пароль
    @Test
    void shouldNotLoginWithEmptyPassword() {
        RegistrationDto user = DataGenerator.createActiveUser();

        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue("");
        $("[data-test-id='action-login']").click();

        // Проверяем, что появилось сообщение об ошибке валидации
        $("[data-test-id='password'].input_invalid").shouldBe(visible);
        $("[data-test-id='password'] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }
}