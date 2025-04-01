package com.example.teamcity.api.generators;

import com.example.teamcity.api.annotations.Optional;
import com.example.teamcity.api.annotations.Parameterizable;
import com.example.teamcity.api.annotations.Random;
import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.models.TestData;
import io.qameta.allure.Step;
import lombok.Builder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for generating test data objects using reflection and annotations.
 * <p>
 * Fields can be filled based on the presence of {@link Random}, {@link Optional}, and {@link Parameterizable} annotations.
 * Supports parameterized input and automatic generation of nested {@link BaseModel} objects.
 */

public final class TestDataGenerator {

    private TestDataGenerator() {
    }

    /**
     * Generates a new test entity of the given model class with optional parameterized values.
     *
     * @param generatedModels a list of already generated models to support reuse and avoid duplication
     * @param generatorClass  the class to instantiate
     * @param parameters      optional parameters to assign to fields marked as {@link Parameterizable}
     * @param <T>             the type of model to generate
     * @return generated model instance
     */
    @Step("Generate instance of class {generatorClass}")
    public static <T extends BaseModel> T generateTestData(List<BaseModel> generatedModels, Class<T> generatorClass, Object... parameters) {
        try {
            var instance = generatorClass.getDeclaredConstructor().newInstance();
            var paramIndex = 0;

            for (Field field : generatorClass.getDeclaredFields()) {
                field.setAccessible(true);
                boolean isParamAvailable = parameters.length > paramIndex;
                boolean hasDefault = field.isAnnotationPresent(Builder.Default.class);
                boolean isOptional = field.isAnnotationPresent(Optional.class);

                if (isOptional && !isParamAvailable && !hasDefault) {
                    field.set(instance, null);
                    continue;
                }

                if (field.isAnnotationPresent(Parameterizable.class) && isParamAvailable) {
                    field.set(instance, parameters[paramIndex]);
                    paramIndex++;
                    continue;
                }

                if (field.isAnnotationPresent(Random.class) && field.getType().equals(String.class)) {
                    field.set(instance, RandomData.getRandomStringWithTestPrefix());
                    continue;
                }

                if (field.getType().equals(Boolean.class)) {
                    if (isParamAvailable) {
                        field.set(instance, parameters[paramIndex]);
                        paramIndex++;
                    } else if (!isOptional || hasDefault) {
                        field.set(instance, field.get(instance));
                    } else {
                        field.set(instance, null);
                    }
                    continue;
                }

                if (field.getType().equals(String.class)) {
                    if (isParamAvailable) {
                        field.set(instance, parameters[paramIndex]);
                        paramIndex++;
                    } else if (!isOptional || hasDefault) {
                        field.set(instance, field.get(instance));
                    } else {
                        field.set(instance, null);
                    }
                    continue;
                }

                if (List.class.isAssignableFrom(field.getType())) {
                    var genericType = (Class<?>) ((java.lang.reflect.ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];

                    if (BaseModel.class.isAssignableFrom(genericType)) {
                        List<BaseModel> generatedList = new ArrayList<>();

                        if (isParamAvailable) {
                            field.set(instance, parameters[paramIndex]);
                        } else if (hasDefault) {
                            field.set(instance, field.get(instance));
                        } else if (!isOptional) {
                            generatedList.add(generateTestData(generatedModels, genericType.asSubclass(BaseModel.class)));
                            field.set(instance, generatedList);
                        } else {
                            field.set(instance, null);
                        }
                        if (isParamAvailable) paramIndex++;
                    } else {
                        field.set(instance, isParamAvailable ? parameters[paramIndex] : (hasDefault ? field.get(instance) : (isOptional ? null : List.of())));
                        if (isParamAvailable) paramIndex++;
                    }
                    continue;
                }

                if (BaseModel.class.isAssignableFrom(field.getType())) {
                    if (isParamAvailable) {
                        field.set(instance, parameters[paramIndex]);
                        paramIndex++;
                    } else if (hasDefault) {
                        field.set(instance, field.get(instance));
                    } else if (!isOptional) {
                        var generatedClass = generateTestData(generatedModels, field.getType().asSubclass(BaseModel.class));
                        field.set(instance, generatedClass);
                    } else {
                        field.set(instance, null);
                    }
                    continue;
                }
            }


            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new IllegalStateException("Failed to generate TestData: reflection error during field population", e);
        }
    }

    /**
     * Generates a fully populated instance of {@link TestData}, initializing all its BaseModel fields.
     *
     * @return generated TestData instance
     */
    @Step("Generate TestData with all nested BaseModel fields")
    public static TestData generateTestData() {
        try {
            var instance = TestData.class.getDeclaredConstructor().newInstance();
            var generatedModels = new ArrayList<BaseModel>();
            for (var field : TestData.class.getDeclaredFields()) {
                field.setAccessible(true);
                if (BaseModel.class.isAssignableFrom(field.getType())) {
                    var generatedModel = generateTestData(generatedModels, field.getType().asSubclass(BaseModel.class));
                    field.set(instance, generatedModel);
                    generatedModels.add(generatedModel);
                }
                field.setAccessible(false);
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new IllegalStateException("Failed to generate TestData: reflection error during field population", e);
        }
    }

    /**
     * Shortcut for generating models without a predefined list of generated models.
     *
     * @param generatorClass model class to instantiate
     * @param parameters     optional field values
     * @param <T>            type extending BaseModel
     * @return generated model instance
     */
    @Step("Generate model {generatorClass} with parameters")
    public static <T extends BaseModel> T generateTestData(Class<T> generatorClass, Object... parameters) {
        return generateTestData(Collections.emptyList(), generatorClass, parameters);
    }
}
