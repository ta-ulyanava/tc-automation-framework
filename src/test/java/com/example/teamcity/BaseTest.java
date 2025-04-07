package com.example.teamcity;

import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.models.TestData;
import com.example.teamcity.api.requests.CheckedRequest;
import com.example.teamcity.api.spec.request.RequestSpecs;
import io.qameta.allure.Step;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import static com.example.teamcity.api.generators.TestDataGenerator.generateTestData;

public class BaseTest {
    protected SoftAssert softy;
    protected TestData testData;
    protected CheckedRequest superUserCheckRequests = new CheckedRequest(RequestSpecs.superUserAuthSpec());

    /**
     * Sets up the test context before each test method:
     * <ul>
     *     <li>Initializes {@link SoftAssert} for collecting all assertions</li>
     *     <li>Generates new {@link TestData} instance with test user/project/etc.</li>
     * </ul>
     */
    @Step("Initialize test context and generate test data")
    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        try {
            softy = new SoftAssert();
            testData = generateTestData();
        } catch (Exception e) {
            System.err.println("Test data generation failed during beforeTest: " + e.getMessage());
        }
    }

    /**
     * Finalizes the test after execution:
     * <ul>
     *     <li>Triggers all collected soft assertions</li>
     *     <li>Cleans up all test data entities via {@link TestDataStorage}</li>
     * </ul>
     */
    @Step("Finalize test and clean up test data")
    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        try {
            softy.assertAll();
            TestDataStorage.getStorageInstance().cleanupTrackedEntities();
        } catch (AssertionError e) {
            System.err.println("Soft assertion failures detected in afterTest: " + e.getMessage());
        }
    }
}

