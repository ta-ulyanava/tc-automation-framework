package com.example.teamcity.api.helpers;

import com.example.teamcity.api.enums.ApiEndpoint;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.CheckedRequest;
import com.example.teamcity.api.responses.ResponseExtractor;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class ProjectHelper {
    /**
     * Creates a single project via API.
     *
     * @param request request handler
     * @param project project model to create
     * @return created project
     */
    @Step("Create single project: {project.name}")
    public Project createProject(CheckedRequest request, Project project) {
        Response response = (Response) request.getRequest(ApiEndpoint.PROJECTS).create(project);
        return ResponseExtractor.extractModel(response, Project.class);
    }
}
