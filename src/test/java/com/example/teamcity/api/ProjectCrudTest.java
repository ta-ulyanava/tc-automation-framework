package com.example.teamcity.api;
import com.example.teamcity.api.enums.ApiEndpoint;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.CheckedRequest;
import com.example.teamcity.api.spec.RequestSpecs;
import org.testng.annotations.Test;


@Test(groups = {"Regression"})
public class ProjectCrudTest extends BaseApiTest {


    @Test(description = "User should be able to create a project with the minimum required fields under Root project", groups = {"Positive", "CRUD"})
    public void userCreatesProjectWithMandatoryFieldsOnlyTest() {
        Project project = testData.getProject();
        Project createdProject = userCheckedRequest.getRequest(ApiEndpoint.PROJECTS, Project.class).create(project).as(Project.class);

        softy.assertEquals(createdProject.getId(), project.getId(), "Project ID should match");
        softy.assertEquals(createdProject.getName(), project.getName(), "Project name should match");
        softy.assertNotNull(createdProject.getParentProject(), "Parent project should not be null");
        softy.assertEquals(createdProject.getParentProject().getId(), "_Root", "Parent project should be '_Root' by default");

        softy.assertAll();
    }


}
