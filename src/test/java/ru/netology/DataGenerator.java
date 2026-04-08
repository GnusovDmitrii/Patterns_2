package ru.netology;

import java.util.UUID;

public class DataGenerator {

    private DataGenerator() {}

    public static RegistrationDto generateActiveUser() {
        String login = UUID.randomUUID().toString().substring(0, 8);
        String password = UUID.randomUUID().toString().substring(0, 8);
        return new RegistrationDto(login, password, "active");
    }

    public static RegistrationDto generateBlockedUser() {
        String login = UUID.randomUUID().toString().substring(0, 8);
        String password = UUID.randomUUID().toString().substring(0, 8);
        return new RegistrationDto(login, password, "blocked");
    }

    public static RegistrationDto generateUserWithLogin(String login) {
        String password = UUID.randomUUID().toString().substring(0, 8);
        return new RegistrationDto(login, password, "active");
    }

    public static RegistrationDto generateUserWithPassword(String password) {
        String login = UUID.randomUUID().toString().substring(0, 8);
        return new RegistrationDto(login, password, "active");
    }
}