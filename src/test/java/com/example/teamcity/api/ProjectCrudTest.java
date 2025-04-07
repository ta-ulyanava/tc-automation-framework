package com.example.teamcity.api;

import com.example.teamcity.api.constants.TestConstants;
import com.example.teamcity.api.enums.ApiEndpoint;
import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.UncheckedRequest;
import com.example.teamcity.api.spec.request.RequestSpecs;
import com.example.teamcity.api.spec.response.IncorrectDataSpecs;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static com.example.teamcity.api.constants.TestConstants.SQL_INJECTION_PAYLOAD;
import static com.example.teamcity.api.constants.TestConstants.XSS_PAYLOAD;


//TODO: Add Github/ gilab CI/CD workflow?
//TODO: Extend fraimework with another endpoints (e.g., search, auth
//TODO: Add UI tests
//TODO: Add DB tests
//TODO: Add performance tests (Jmeter)
// TODO: add mocks ?

@Test(groups = {"Regression"})
public class ProjectCrudTest extends BaseApiTest {

    // =================== PROJECT CREATION =================== //
    @Feature("Projects creation")
    @Story("Create project with required fields only")
    @Test(description = "User should be able to create a project with the minimum required fields under Root project", groups = {"Positive", "CRUD"})
    public void userCreatesProjectWithMandatoryFieldsOnlyTest() {
        Project project = testData.getProject();
        Project createdProject = projectHelper.createProject(userCheckedRequest, project);
        EntityValidator.validateAllEntityFieldsIgnoring(project, createdProject, List.of("parentProject"), softy);
        softy.assertEquals(createdProject.getParentProject().getId(), TestConstants.ROOT_PROJECT_ID, "Parent project should be '_Root' by default");
        softy.assertAll();
    }

    // =================== PROJECT NAME VALIDATION =================== //
    @Feature("Project Name Validation")
    @Story("Empty Project Name")
    @Test(description = "User should not be able to create Project with empty name", groups = {"Negative", "CRUD"})
    public void userCannotCreateProjectWithEmptyNameTest() {
        Project invalidProject = TestDataGenerator.generateTestData(List.of(), Project.class, RandomData.getRandomStringWithTestPrefix(), "");
        Response response = userUncheckedRequest.getRequest(ApiEndpoint.PROJECTS).create(invalidProject);
        response.then().spec(IncorrectDataSpecs.badRequestEmptyField("Project", "name"));
        softy.assertAll();
    }
    @Feature("Project Name Validation")
    @Story("Space in Project Name")
    @Test(description = "User should not be able to create Project with name that is just a space", groups = {"Negative", "CRUD","KnownBugs"})
    @Issue("Bug in API: returned 500 error instead of 400")
    public void userCannotCreateProjectWithSpaceOnlyNameTest() {
        Project invalidProject = TestDataGenerator.generateTestData(List.of(), Project.class, RandomData.getRandomStringWithTestPrefix(), " ");
        Response response = userUncheckedRequest.getRequest(ApiEndpoint.PROJECTS).create(invalidProject);
        response.then().spec(IncorrectDataSpecs.badRequestEmptyField("project", "name"));
        softy.assertAll();
    }
    @Feature("Project Name Validation")
    @Story("Special Characters in Project Name")
    @Test(description = "User should be able to create a Project with special characters in name", groups = {"Positive", "CRUD"})
    public void userCreatesProjectWithSpecialCharactersInNameTest() {
        Project project = TestDataGenerator.generateTestData(Project.class, RandomData.getRandomStringWithTestPrefix(), TestConstants.SPECIAL_CHARACTERS);
        Project createdProject = projectHelper.createProject(superUserCheckRequests, project);
        EntityValidator.validateAllEntityFieldsIgnoring(project, createdProject, List.of("parentProject"), softy);
        softy.assertAll();
    }

    @Feature("Project Name Validation")
    @Story("Localized Project Name")
    @Test(description = "User should be able to create a Project with a localized name", groups = {"Positive", "CRUD"})
    public void userCreatesProjectWithLocalizedNameTest() {
        Project localizedProject = TestDataGenerator.generateTestData(Project.class, RandomData.getRandomStringWithTestPrefix(), TestConstants.LOCALIZATION_CHARACTERS);
        Project createdProject = projectHelper.createProject(superUserCheckRequests, localizedProject);
        EntityValidator.validateAllEntityFieldsIgnoring(localizedProject, createdProject, List.of("parentProject"), softy);
        softy.assertAll();
    }

    @Feature("Project Name Validation")
    @Story("One Character Project Name")
    @Test(description = "User should be able to create a Project with a name of length 1", groups = {"Positive", "CRUD"})
    public void userCreatesProjectWithOneCharacterNameTest() {
        Project validProject = TestDataGenerator.generateTestData(Project.class, RandomData.getRandomStringWithTestPrefix(), "A");
        Project createdProject = projectHelper.createProject(superUserCheckRequests, validProject);
        EntityValidator.validateAllEntityFieldsIgnoring(validProject, createdProject, List.of("parentProject"), softy);
        softy.assertAll();
    }

    @Feature("Project Name Validation")
    @Story("Maximum Length Project Name")
    @Test(description = "User should be able to create a Project with a name of 500 characters", groups = {"Positive", "CRUD", "CornerCase"})
    public void userCreatesProjectWith500LengthNameTest() {
        String maxLengthName = RandomData.getRandomString(500);
        Project validProject = TestDataGenerator.generateTestData(Project.class, RandomData.getRandomStringWithTestPrefix(), maxLengthName);
        Project createdProject = projectHelper.createProject(superUserCheckRequests, validProject);
        EntityValidator.validateAllEntityFieldsIgnoring(validProject, createdProject, List.of("parentProject"), softy);
        softy.assertAll();
    }

    @Feature("Project Name Validation")
    @Story("Duplicate Name with Different Case")
    @Test(description = "User should not be able to create a Project with an existing name in a different case", groups = {"Negative", "CRUD"})
    public void userCannotCreateProjectWithExistingNameDifferentCaseTest() {
        Project existingProject = projectHelper.createProject(userCheckedRequest, testData.getProject());
        String duplicateName = existingProject.getName().toUpperCase();
        Project duplicateProject = TestDataGenerator.generateTestData(Project.class, RandomData.getRandomStringWithTestPrefix(), duplicateName);
        Response response = userUncheckedRequest.getRequest(ApiEndpoint.PROJECTS).create(duplicateProject);
        response.then().spec(IncorrectDataSpecs.badRequestDuplicatedField("Project", "name", duplicateName));
        softy.assertAll();
    }

    @Feature("Project Name Validation")
    @Story("Duplicate Name")
    @Test(description = "User should not be able to create a Project with an existing name", groups = {"Negative", "CRUD"})
    public void userCannotCreateProjectWithExistingNameTest() {
        Project existingProject = projectHelper.createProject(userCheckedRequest, testData.getProject());
        Project duplicateProject = TestDataGenerator.generateTestData(Project.class, RandomData.getRandomStringWithTestPrefix(), existingProject.getName());
        Response response = userUncheckedRequest.getRequest(ApiEndpoint.PROJECTS).create(duplicateProject);
        response.then().spec(IncorrectDataSpecs.badRequestDuplicatedField("Project", "name", existingProject.getName()));
        softy.assertAll();
    }

    @Feature("Project Name Validation")
    @Story("Digits Only Name")
    @Test(description = "User should be able to create a Project with a name consisting only of digits", groups = {"Positive", "CRUD"})
    public void userCreatesProjectWithDigitsOnlyNameTest() {
        Project validProject = TestDataGenerator.generateTestData(Project.class, RandomData.getUniqueIdWithTestPrefix(), RandomData.getRandomDigits(6));
        Project createdProject = projectHelper.createProject(superUserCheckRequests, validProject);
        EntityValidator.validateAllEntityFieldsIgnoring(validProject, createdProject, List.of("parentProject"), softy);
        softy.assertAll();
    }

    @Feature("Project Name Validation")
    @Story("Spaces In Name")
    @Test(description = "User should be able to create a Project with spaces in the middle of the name", groups = {"Positive", "CRUD"})
    public void userCreatesProjectWithSpacesInNameTest() {
        String uniqueProjectName = RandomData.getUniqueNameWithTestPrefix().substring(0, 5) + " " + RandomData.getUniqueNameWithTestPrefix().substring(5);
        Project validProject = TestDataGenerator.generateTestData(Project.class, RandomData.getUniqueIdWithTestPrefix(), uniqueProjectName);
        Project createdProject = projectHelper.createProject(superUserCheckRequests, validProject);
        EntityValidator.validateAllEntityFieldsIgnoring(validProject, createdProject, List.of("parentProject"), softy);
        softy.assertAll();
    }


// =================== PROJECT NAME VALIDATION =================== //
// =================== AUTHORIZATIONS TESTS  =================== //
@Feature("Authorization")
@Story("User without authentication should not create a project")
@Test(description = "User should not be able to create a project without authentication", groups = {"Negative", "Auth"})
public void userCannotCreateProjectWithoutAuthTest() {
    var unauthRequest = new UncheckedRequest(RequestSpecs.unauthSpec());
    var invalidProject = TestDataGenerator.generateTestData(List.of(), Project.class, RandomData.getRandomStringWithTestPrefix(), RandomData.getRandomStringWithTestPrefix());
    var response = unauthRequest.getRequest(ApiEndpoint.PROJECTS).create(invalidProject);
    response.then().spec(com.example.teamcity.api.spec.responce.AccessErrorSpecs.authenticationRequired());
    softy.assertAll();
}

    // =================== AUTHORIZATIONS TESTS (AUTH_TAG) =================== //

    // =================== SECURITY TESTS =================== //
// Validate protection against XSS and SQL injection in the project name
// Expect the server to store the payload as plain text without executing any scripts
// No need to perform such checks for the ID field, since it is already validated against special characters


    @Feature("Security")
    @Story("XSS Injection Prevention")
    @Test(description = "User should be able to create a Project with an XSS payload in name (payload stored as text)", groups = {"Positive", "Security", "CRUD"})
    public void userCreatesProjectWithXSSInNameTest() {
        Project projectWithXSS = TestDataGenerator.generateTestData(Project.class, RandomData.getUniqueIdWithTestPrefix(), XSS_PAYLOAD);
        Project createdProject = projectHelper.createProject(superUserCheckRequests, projectWithXSS);
        EntityValidator.validateAllEntityFieldsIgnoring(projectWithXSS, createdProject, List.of("parentProject"), softy);
        softy.assertAll();
    }


    @Feature("Security")
    @Story("SQL Injection Prevention")
    @Test(description = "User should be able to create a Project with an SQL injection payload in name (payload stored as text)", groups = {"Positive", "Security", "CRUD"})
    public void userCreatesProjectWithSQLInjectionTest() {
        Project projectWithSQL = TestDataGenerator.generateTestData(Project.class, RandomData.getUniqueIdWithTestPrefix(), SQL_INJECTION_PAYLOAD);
        Project createdProject = projectHelper.createProject(superUserCheckRequests, projectWithSQL);
        EntityValidator.validateAllEntityFieldsIgnoring(projectWithSQL, createdProject, List.of("parentProject"), softy);
        softy.assertAll();
    }

// =================== SECURITY TESTS (SEC_TAG) =================== //

// =================== PROJECT CREATION ===================
// Test: create project with required fields only
// - Ensure project is created when only ID and name are provided
// - If no parent is specified, it should default to _Root



// =================== COPY SETTINGS ===================
// Test: create project with copyAllAssociatedSettings = true
// - Validate that the following fields are copied:
//   - projectsIdsMap
//   - buildTypesIdsMap
//   - vcsRootsIdsMap
//   - sourceProject
// Note: Known API bug – settings are not copied as expected

// Test: create project with copyAllAssociatedSettings = false
// - Ensure the following fields are not present in the created project:
//   - copyAllAssociatedSettings
//   - sourceProject
//   - projectsIdsMap
//   - buildTypesIdsMap
//   - vcsRootsIdsMap

// =================== PROJECT HIERARCHY ===================
// Test: create nested projects
// - Validate a linear hierarchy of N nested projects
// - Ensure names of first and last match the input

// Test: create sibling projects
// - Create N sibling projects under the same parent
// - Validate correct sibling hierarchy under root

// =================== PROJECT ID VALIDATION ===================
// Test: create project with max-length ID (225 chars)
// Test: create project with min-length ID (1 char)
// Test: create project with underscore in ID
// Test: cannot create project with ID > 225 chars (expect error)
// Test: cannot create project with special characters in ID (data-driven)
// Test: cannot create project with non-Latin ID (data-driven)
// Test: cannot create project with empty ID
// Test: cannot create project with space as ID
// Test: cannot create project with duplicate ID (case-sensitive and insensitive)
// Test: cannot create project with digits-only ID
// Test: cannot create project with ID starting with digit or underscore (data-driven)
// Test: cannot create project with space in the middle of ID
// Test: create project with valid ID (letters, digits, underscores)


// =================== PARENT PROJECT VALIDATION ===================
// Test: cannot create project with non-existent parent project locator
// Test: cannot create project where project ID == parent ID
// Test: cannot create project with empty parent locator


// =================== ROLE-BASED ACCESS CONTROL ===================
// Test: user with allowed role (e.g., PROJECT_ADMIN) can create regular project
// Test: user with restricted role (e.g., VIEWER, DEVELOPER) cannot create regular project
// Test: user with allowed role can create nested project
// Test: user with restricted role cannot create nested project


}
