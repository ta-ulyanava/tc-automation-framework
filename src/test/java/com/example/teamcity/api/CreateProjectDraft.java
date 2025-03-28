package com.example.teamcity.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.Map;


public class CreateProjectDraft {

    public static void main(String[] args) throws Exception{
        RestAssured.baseURI = "http://192.168.1.34:8111";
        RestAssured.authentication = RestAssured.basic("admin", "admin");

        Map<String, Object> payload = Map.of(
                "parentProject", Map.of("locator", "_Root"),
                "name", "project100",
                "id", "project100",
                "copyAllAssociatedSettings", true
        );

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(payload);

        RestAssured
                .given()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body(jsonBody)
                .when()
                    .post("/app/rest/projects")
                .then()
                    .log().all()
                    .extract().response();

    }
}
