package com.example.teamcity.api.requests.baseRequests;

import com.example.teamcity.api.enums.ApiEndpoint;
import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Base implementation of CRUD and search operations for TeamCity API without response validation.
 * <p>
 * Designed for use in negative, exploratory, or technical tests where strict response schema validation is not required.
 * Provides raw access to TeamCity REST API endpoints with basic request handling.
 */
public class UncheckedBase extends Request implements CrudInterface {

    public UncheckedBase(RequestSpecification spec, ApiEndpoint apiEndpoint) {
        super(spec, apiEndpoint);
    }

    @Step("RAW: Create entity at {apiEndpoint}")
    @Override
    public Response create(BaseModel model) {
        return RestAssured
                .given()
                .spec(spec)
                .body(model)
                .post(apiEndpoint.getUrl());
    }

    @Step("RAW: Read entity from {apiEndpoint} with locator: {idOrLocator}")
    @Override
    public Response read(String idOrLocator) {
        return RestAssured
                .given()
                .spec(spec)
                .get(apiEndpoint.getUrl() + "/" + idOrLocator);
    }

    @Step("RAW: Update entity at {apiEndpoint} with locator: {locator}")
    @Override
    public Response update(String locator, BaseModel model) {
        return RestAssured
                .given()
                .spec(spec)
                .body(model)
                .put(apiEndpoint.getUrl() + "/" + locator);
    }

    @Step("RAW: Delete entity from {apiEndpoint} with locator: {locator}")
    @Override
    public Response delete(String locator) {
        return RestAssured
                .given()
                .spec(spec)
                .delete(apiEndpoint.getUrl() + "/" + locator);
    }


}
