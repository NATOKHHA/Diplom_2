package utils;

import models.User;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomDataGenerator {
    public static String randomEmail() {
        return RandomStringUtils.randomAlphanumeric(10) + "@gmail.com";
    }

    public static String randomPassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public static String randomName() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    public static User createRandomUser() {
        return new User(
                randomEmail(),
                randomPassword(),
                randomName()
        );
    }
}