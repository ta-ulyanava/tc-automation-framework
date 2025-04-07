package com.example.teamcity.api;

import com.example.teamcity.BaseTest;
import com.example.teamcity.api.enums.ApiEndpoint;
import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.helpers.ProjectHelper;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.CheckedRequest;
import com.example.teamcity.api.requests.UncheckedRequest;
import com.example.teamcity.api.spec.request.RequestSpecs;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;

public class BaseApiTest extends BaseTest {
    protected UncheckedRequest userUncheckedRequest;
    protected CheckedRequest userCheckedRequest;
    protected UncheckedRequest superUserUncheckedRequest = new UncheckedRequest(RequestSpecs.superUserAuthSpec());
    protected ProjectHelper projectHelper;

    /**
     * Prepares API request objects for the test user before each test method.
     * <p>
     * - Creates a new user via API using superuser credentials<br>
     * - Adds the user to {@link TestDataStorage} for automatic cleanup<br>
     * - Initializes {@link CheckedRequest} and {@link UncheckedRequest} for the created user
     * </p>
     * <p>
     * This method runs before every test and ensures that all user-specific API requests
     * are properly authenticated and ready to use.
     * </p>
     */
    @Step("Configure user-specific Checked and Unchecked requests")
    @BeforeMethod(alwaysRun = true)
    public void configureUserRequests() {
        User createdUser = superUserUncheckedRequest.getRequest(ApiEndpoint.USERS).create(testData.getUser()).as(User.class);
        TestDataStorage.getStorageInstance().addCreatedEntity(ApiEndpoint.USERS, String.valueOf(createdUser.getId()));
        userCheckedRequest = new CheckedRequest(RequestSpecs.authSpec(testData.getUser()));
        userUncheckedRequest = new UncheckedRequest(RequestSpecs.authSpec(testData.getUser()));
        projectHelper = new ProjectHelper();
    }
}
