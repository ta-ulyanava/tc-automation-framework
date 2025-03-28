package com.example.teamcity.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;


public class CreateProjectDraft {

    public static void main(String[] args) {
        RestAssured.baseURI = "http://192.168.1.34:8111";
        RestAssured.authentication = RestAssured.basic("admin", "admin");

        String jsonBody = "{\n" +
                "    \"parentProject\": {\n" +
                "        \"locator\": \"_Root\"\n" +
                "    },\n" +
                "    \"name\": \"project100\",\n" +
                "    \"id\": \"project100\",\n" +
                "    \"copyAllAssociatedSettings\": true\n" +
                "}";

        Response response = RestAssured
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
