package ru.netology;

import java.util.UUID;

public class DataGenerator {

    private DataGenerator() {}

    public static String generateLogin() {
        return "user_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static String generatePassword() {
        return "pass_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static RegistrationDto generateActiveUser() {
        return new RegistrationDto(generateLogin(), generatePassword(), "active");
    }

    public static RegistrationDto generateBlockedUser() {
        return new RegistrationDto(generateLogin(), generatePassword(), "blocked");
    }
}