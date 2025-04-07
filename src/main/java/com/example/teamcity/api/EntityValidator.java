package com.example.teamcity.api;

import io.qameta.allure.Step;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Field;
import java.util.List;
/**
 * Utility class for validating that two entity objects have matching field values.
 */
public class EntityValidator {
    /**
     * Compares all fields of the expected and actual entity objects using soft assertions.
     *
     * @param expected   the expected entity
     * @param actual     the actual entity
     * @param softAssert SoftAssert instance for collecting assertion errors
     * @param <T>        the type of the entity
     */
    @Step("Validate all fields of entity: {expected}")
    public static <T> void validateAllEntityFields(T expected, T actual, SoftAssert softAssert) {
        validateAllEntityFieldsIgnoring(expected, actual, List.of(), softAssert);
    }
    /**
     * Compares all fields of the expected and actual entity objects,
     * ignoring the specified field names, using soft assertions.
     *
     * @param expected      the expected entity
     * @param actual        the actual entity
     * @param ignoredFields list of field names to skip during comparison
     * @param softAssert    SoftAssert instance for collecting assertion errors
     * @param <T>           the type of the entity
     */
    @Step("Validate fields of entity (ignoring fields {ignoredFields}): {expected}")
    public static <T> void validateAllEntityFieldsIgnoring(T expected, T actual, List<String> ignoredFields, SoftAssert softAssert) {
        for (Field field : expected.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (ignoredFields.contains(field.getName())) continue;
            try {
                Object expectedValue = field.get(expected);
                Object actualValue = field.get(actual);
                softAssert.assertEquals(actualValue, expectedValue, "Field '" + field.getName() + "' does not match");
            } catch (IllegalAccessException e) {
                throw new RuntimeException(String.format(
                        "Failed to access field '%s' in class %s. Expected value: [%s], Actual value: [%s]",
                        field.getName(),
                        expected.getClass().getSimpleName(),
                        safeToString(field, expected),
                        safeToString(field, actual)
                ), e);
            }
        }
    }

    private static <T> String safeToString(Field field, T obj) {
        try {
            Object value = field.get(obj);
            return value != null ? value.toString() : "null";
        } catch (Exception e) {
            return "unavailable";
        }
    }
}
