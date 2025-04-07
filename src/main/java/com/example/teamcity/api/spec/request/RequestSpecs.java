package com.example.teamcity.api.spec.request;

import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.models.User;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.List;

/**
 * Utility class for creating different types of RestAssured RequestSpecifications.
 */
public class RequestSpecs {
    /**
     * Creates a base RequestSpecBuilder with content type, accept headers,
     * logging filters and base URI based on the configuration.
     *
     * @return a pre-configured RequestSpecBuilder
     */
    private static RequestSpecBuilder reqBuilder() {
        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setBaseUri("https://" + Config.getProperty("host"));
        reqBuilder.setContentType(ContentType.JSON);
        reqBuilder.setAccept(ContentType.JSON);
        reqBuilder.addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter()));
        return reqBuilder;
    }

    /**
     * Creates a request specification without authentication.
     *
     * @return unauthenticated RequestSpecification
     */
    public static RequestSpecification unauthSpec() {
        return reqBuilder().setRelaxedHTTPSValidation().build();
    }

    /**
     * Creates a request specification for a superuser using a token.
     *
     * @return superuser authenticated RequestSpecification
     */
    public static RequestSpecification superUserAuthSpec() {
        var requestBuilder = reqBuilder();
        requestBuilder.setBaseUri("http://:%s@%s".formatted(Config.getProperty("superUserToken"), Config.getProperty("host")));
        return requestBuilder.build();
    }

    /**
     * Creates a request specification using basic authentication with given user credentials.
     *
     * @param user User object containing credentials
     * @return authenticated RequestSpecification
     */
    public static RequestSpecification authSpec(User user) {
        var requestBuilder = reqBuilder();
        requestBuilder.setBaseUri("http://%s:%s@%s".formatted(user.getUsername(), user.getPassword(), Config.getProperty("host")));
        return requestBuilder.build();
    }
}
