package com.example.teamcity.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Project extends BaseModel{
    public Project(String id, String name, ParentProject parentProject, Boolean copyAllAssociatedSettings, Project sourceProject) {
        this.id = id;
        this.name = name;
        this.parentProject = parentProject;
        this.copyAllAssociatedSettings = copyAllAssociatedSettings;
        this.sourceProject = sourceProject;
    }

    private String id;
    private String name;
    private ParentProject parentProject;
    private Boolean copyAllAssociatedSettings;
    private Map<String, String> projectsIdsMap;
    private Map<String, String> buildTypesIdsMap;
    private Map<String, String> vcsRootsIdsMap;
    private Project sourceProject;
}
