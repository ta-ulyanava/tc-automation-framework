package com.example.teamcity.api;

import com.example.teamcity.api.config.Config;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;


import java.util.List;
import java.util.Map;

@Test
public class CreateProjectDraftTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    Map<String, Object> payload = Map.of(
            "parentProject", Map.of("locator", "_Root"),
            "name", "project100",
            "id", "project100",
            "copyAllAssociatedSettings", true
    );



    public static RequestSpecification getAuthSpec() {
        return RestAssured
                .given()
                    .baseUri("http://" + Config.getProperty("host"))
                    .auth().basic("admin", "admin")
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .filters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter()));
    }
//        RestAssured
//
//                .when()
//                    .post("/app/rest/projects")
//                .then()
//                    .log().all()
//                    .extract().response();
//
//    }
public static void createProject(Map<String, Object> payload) {
    try {
        String jsonBody = objectMapper.writeValueAsString(payload);
        getAuthSpec()
                .body(jsonBody)
                .when()
                    .post("/app/rest/projects")
                .then()
                    .extract().response();
    } catch (Exception e) {
        throw new RuntimeException("Failed to create project", e);
    }
}
}