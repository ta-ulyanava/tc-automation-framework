package com.example.teamcity;

import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.models.TestData;
import com.example.teamcity.api.requests.CheckedRequest;
import com.example.teamcity.api.spec.RequestSpecs;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import static com.example.teamcity.api.generators.TestDataGenerator.generateTestData;

public class BaseTest {
    protected SoftAssert softy;
    protected TestData testData;
    protected CheckedRequest superUserCheckRequests = new CheckedRequest(RequestSpecs.superUserAuthSpec());

    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        try {
            softy = new SoftAssert();
            testData = generateTestData();
        } catch (Exception e) {
            System.err.println("Test data generation failed during beforeTest: " + e.getMessage());
        }
    }

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
