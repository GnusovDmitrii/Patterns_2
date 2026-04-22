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

    @Test
    void shouldLoginWithActiveRegisteredUser() {
        RegistrationDto user = DataGenerator.createActiveUser();

        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $("[data-test-id='action-login']").click();

        // Проверка успешного входа - проверяем видимость заголовка "Личный кабинет"
        $("h2").shouldHave(exactText("Личный кабинет")).shouldBe(visible);

        // Дополнительно проверяем, что сообщение об ошибке не появилось
        $("[data-test-id='error-notification']").shouldNotBe(visible);
    }

    @Test
    void shouldNotLoginWithBlockedRegisteredUser() {
        RegistrationDto user = DataGenerator.createBlockedUser();

        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $("[data-test-id='action-login']").click();

        // Проверка сообщения об ошибке
        $("[data-test-id='error-notification']").shouldBe(visible);
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(exactText("Ошибка! Пользователь заблокирован"));
    }

    @Test
    void shouldNotLoginWithUnregisteredActiveUser() {
        String login = DataGenerator.generateLogin();
        String password = DataGenerator.generatePassword();

        $("[data-test-id='login'] input").setValue(login);
        $("[data-test-id='password'] input").setValue(password);
        $("[data-test-id='action-login']").click();

        // Проверка сообщения об ошибке
        $("[data-test-id='error-notification']").shouldBe(visible);
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(exactText("Ошибка! Неверно указан логин или пароль"));

        // Проверяем, что заголовок "Личный кабинет" не появился
        $("h2").shouldNotHave(exactText("Личный кабинет"));
    }

    @Test
    void shouldNotLoginWithValidUserWrongPassword() {
        RegistrationDto user = DataGenerator.createActiveUser();

        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue("wrong_password");
        $("[data-test-id='action-login']").click();

        // Проверка сообщения об ошибке
        $("[data-test-id='error-notification']").shouldBe(visible);
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLoginWithValidUserWrongLogin() {
        RegistrationDto user = DataGenerator.createActiveUser();

        $("[data-test-id='login'] input").setValue("wrong_" + user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $("[data-test-id='action-login']").click();

        // Проверка сообщения об ошибке
        $("[data-test-id='error-notification']").shouldBe(visible);
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLoginWithEmptyLogin() {
        RegistrationDto user = DataGenerator.createActiveUser();

        $("[data-test-id='login'] input").setValue("");
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $("[data-test-id='action-login']").click();

        // Проверка валидации поля
        $("[data-test-id='login'].input_invalid").shouldBe(visible);
        $("[data-test-id='login'] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));

        // Сообщение об ошибке от сервера не должно появляться при валидации на клиенте
        $("[data-test-id='error-notification']").shouldNotBe(visible);

        // Заголовок "Личный кабинет" не должен появиться
        $("h2").shouldNotHave(exactText("Личный кабинет"));
    }

    @Test
    void shouldNotLoginWithEmptyPassword() {
        RegistrationDto user = DataGenerator.createActiveUser();

        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue("");
        $("[data-test-id='action-login']").click();

        // Проверка валидации поля
        $("[data-test-id='password'].input_invalid").shouldBe(visible);
        $("[data-test-id='password'] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));

        // Сообщение об ошибке от сервера не должно появляться
        $("[data-test-id='error-notification']").shouldNotBe(visible);

        // Заголовок "Личный кабинет" не должен появиться
        $("h2").shouldNotHave(exactText("Личный кабинет"));
    }
}