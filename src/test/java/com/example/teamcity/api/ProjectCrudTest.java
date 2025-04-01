package com.example.teamcity.api;

import com.example.teamcity.api.enums.ApiEndpoint;
import com.example.teamcity.api.models.Project;
import org.testng.annotations.Test;


@Test(groups = {"Regression"})
public class ProjectCrudTest extends BaseApiTest {


    @Test(description = "User should be able to create a project with the minimum required fields under Root project", groups = {"Positive", "CRUD"})
    public void userCreatesProjectWithMandatoryFieldsOnlyTest() {
        Project project = testData.getProject();
        Project createdProject = userCheckedRequest.getRequest(ApiEndpoint.PROJECTS, Project.class).create(project).as(Project.class);
// TODO: Add assertions to verify the project creation via DTO
        softy.assertEquals(createdProject.getId(), project.getId(), "Project ID should match");
        softy.assertEquals(createdProject.getName(), project.getName(), "Project name should match");
        softy.assertNotNull(createdProject.getParentProject(), "Parent project should not be null");
        // TODO: ?Remove magic string _Root?
        softy.assertEquals(createdProject.getParentProject().getId(), "_Root", "Parent project should be '_Root' by default");
        softy.assertAll();
    }
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

// =================== PROJECT NAME VALIDATION ===================
// Test: cannot create project with empty name
// Test: cannot create project with name that is just a space
// Test: create project with special characters in name
// Test: create project with localized name
// Test: create project with 1-character name
// Test: create project with 500-character name
// Test: cannot create project with duplicate name (case-sensitive and insensitive)
// Test: create project with digits-only name
// Test: create project with spaces in the middle of the name

// =================== PARENT PROJECT VALIDATION ===================
// Test: cannot create project with non-existent parent project locator
// Test: cannot create project where project ID == parent ID
// Test: cannot create project with empty parent locator

// =================== AUTHORIZATION ===================
// Test: unauthenticated user cannot create a project

// =================== SECURITY ===================
// Test: create project with XSS payload in name (should be stored as plain text)
// Test: create project with SQL injection payload in name (should be stored as plain text)

// =================== ROLE-BASED ACCESS CONTROL ===================
// Test: user with allowed role (e.g., PROJECT_ADMIN) can create regular project
// Test: user with restricted role (e.g., VIEWER, DEVELOPER) cannot create regular project
// Test: user with allowed role can create nested project
// Test: user with restricted role cannot create nested project


}
