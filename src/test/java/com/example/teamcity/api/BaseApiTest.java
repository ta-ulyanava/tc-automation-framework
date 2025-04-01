package com.example.teamcity.api;

import com.example.teamcity.BaseTest;
import com.example.teamcity.api.enums.ApiEndpoint;
import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.CheckedRequest;
import com.example.teamcity.api.requests.UncheckedRequest;
import com.example.teamcity.api.spec.RequestSpecs;
import org.testng.annotations.BeforeMethod;

public class BaseApiTest extends BaseTest {
    protected UncheckedRequest userUncheckedRequest;
    protected CheckedRequest userCheckedRequest;
    protected UncheckedRequest superUserUncheckedRequest = new UncheckedRequest(RequestSpecs.superUserAuthSpec());

    @BeforeMethod(alwaysRun = true)
    public void configureUserRequests() {
        User createdUser = superUserUncheckedRequest.getRequest(ApiEndpoint.USERS).create(testData.getUser()).as(User.class);
        TestDataStorage.getStorageInstance().addCreatedEntity(ApiEndpoint.USERS, String.valueOf(createdUser.getId()));
        userCheckedRequest = new CheckedRequest(RequestSpecs.authSpec(testData.getUser()));
        userUncheckedRequest = new UncheckedRequest(RequestSpecs.authSpec(testData.getUser()));
    }
}
