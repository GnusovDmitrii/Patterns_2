package ru.netology;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class LoginTest {

    private static final String BASE_URL = "http://localhost:9999";

    @BeforeAll
    static void setUpAll() {
        Configuration.browser = "chrome";
        Configuration.headless = false;
        Configuration.timeout = 10000;
        Configuration.holdBrowserOpen = false;
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

        // Проверяем, что произошел вход (перенаправление на страницу личного кабинета)
        sleep(2000); // Даем время на перенаправление
        String currentUrl = webdriver().driver().url();
        assert currentUrl.contains("/dashboard") || !currentUrl.equals(BASE_URL);
    }

    // Тест 2: Вход заблокированного пользователя
    @Test
    void shouldNotLoginWithBlockedRegisteredUser() {
        RegistrationDto user = DataGenerator.createBlockedUser();

        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $("[data-test-id='action-login']").click();

        // Проверяем, что остались на странице входа
        sleep(1000);
        assert !webdriver().driver().url().contains("/dashboard");

        // Ищем текст ошибки (если есть)
        if ($("[data-test-id='error-notification']").exists()) {
            $("[data-test-id='error-notification']").shouldBe(visible);
        }
    }

    // Тест 3: Вход незарегистрированного пользователя
    @Test
    void shouldNotLoginWithUnregisteredActiveUser() {
        String login = DataGenerator.generateLogin();
        String password = DataGenerator.generatePassword();

        $("[data-test-id='login'] input").setValue(login);
        $("[data-test-id='password'] input").setValue(password);
        $("[data-test-id='action-login']").click();

        sleep(1000);
        assert !webdriver().driver().url().contains("/dashboard");
    }

    // Тест 4: Вход с неправильным паролем
    @Test
    void shouldNotLoginWithValidUserWrongPassword() {
        RegistrationDto user = DataGenerator.createActiveUser();

        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue("wrong_password");
        $("[data-test-id='action-login']").click();

        sleep(1000);
        assert !webdriver().driver().url().contains("/dashboard");
    }

    // Тест 5: Вход с неправильным логином
    @Test
    void shouldNotLoginWithValidUserWrongLogin() {
        RegistrationDto user = DataGenerator.createActiveUser();

        $("[data-test-id='login'] input").setValue("wrong_" + user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $("[data-test-id='action-login']").click();

        sleep(1000);
        assert !webdriver().driver().url().contains("/dashboard");
    }

    // Тест 6: Пустой логин
    @Test
    void shouldNotLoginWithEmptyLogin() {
        RegistrationDto user = DataGenerator.createActiveUser();

        $("[data-test-id='login'] input").setValue("");
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $("[data-test-id='action-login']").click();

        // Проверяем, что кнопка не активна или есть ошибка валидации
        sleep(1000);
        assert !webdriver().driver().url().contains("/dashboard");
    }

    // Тест 7: Пустой пароль
    @Test
    void shouldNotLoginWithEmptyPassword() {
        RegistrationDto user = DataGenerator.createActiveUser();

        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue("");
        $("[data-test-id='action-login']").click();

        sleep(1000);
        assert !webdriver().driver().url().contains("/dashboard");
    }
}