package com.example.teamcity.api.models;

import lombok.Data;

/**
 * Container that holds all necessary test entities : user, project, and build configuration.
 */
@Data
public class TestData {
    private Project project;
    private User user;
    private BuildType buildType;

}
