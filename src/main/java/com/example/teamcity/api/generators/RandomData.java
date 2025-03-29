package com.example.teamcity.api.generators;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

/**
 * Utility class for generating random test data, such as strings, numeric values, unique names and IDs.
 * All generated values are prefixed with 'test_' for clarity in test environments.
 */

public final class RandomData {
    private static final String TEST_PREFIX = "test_";
    private static final int MAX_LENGTH = 10;
    private static final Random RANDOM = new Random();

    private RandomData() {
    }

    public static String randomString() {
        return TEST_PREFIX + RandomStringUtils.randomAlphanumeric(MAX_LENGTH);
    }

    public static String randomString(int length) {
        return TEST_PREFIX + RandomStringUtils.randomAlphanumeric(Math.max(length - TEST_PREFIX.length(), 1));
    }

    public static String uniqueName() {
        return TEST_PREFIX + System.currentTimeMillis() + RandomStringUtils.randomAlphanumeric(MAX_LENGTH);
    }

    public static String uniqueId() {
        return TEST_PREFIX + System.currentTimeMillis() + RandomStringUtils.randomAlphanumeric(MAX_LENGTH);
    }

    public static String randomNumericString(int length) {
        return RandomStringUtils.randomNumeric(length);
    }

    public static int randomInt(int bound) {return RANDOM.nextInt(bound);}

    public static String randomAlphaString(int length) {return RandomStringUtils.randomAlphabetic(length);
    }

}
