package com.example.teamcity.api.spec.response;

import io.qameta.allure.Step;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.*;

public class IncorrectDataSpecs {


    @Step("Expect 400 Bad Request due to empty field '{field}'")
    public static ResponseSpecification badRequestEmptyField(String entityType, String field) {
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_BAD_REQUEST)
                .expectBody(anyOf(
                        containsString("%s %s cannot be empty.".formatted(entityType, field)),
                        containsString("Given %s %s is empty.".formatted(entityType, field))
                ))
                .build();
    }
    @Step("Expect 400 Bad Request due to duplicated value '{value}' in field '{field}'")
    public static ResponseSpecification badRequestDuplicatedField(String entityType, String field, String value) {
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_BAD_REQUEST)
                .expectBody(anyOf(
                        containsString("%s %s \"%s\" is already used".formatted(entityType, field, value)),
                        containsString("%s with this %s already exists: %s".formatted(entityType, field, value))
                ))
                .build();
    }

}
