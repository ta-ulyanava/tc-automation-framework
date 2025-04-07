package com.example.teamcity.api.generators;

import com.example.teamcity.api.enums.ApiEndpoint;
import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.requests.baseRequests.UncheckedBase;
import com.example.teamcity.api.spec.request.RequestSpecs;
import io.qameta.allure.Step;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Singleton class that stores references to created test entities for later cleanup.
 * <p>
 * Supports tracking by {@link ApiEndpoint} and deleting all registered entities via API calls.
 */
public class TestDataStorage {
    private static TestDataStorage testDataStorage;
    private final EnumMap<ApiEndpoint, Set<String>> createdEntitiesMap;

    private TestDataStorage() {
        createdEntitiesMap = new EnumMap<>(ApiEndpoint.class);
    }

    public static TestDataStorage getStorageInstance() {
        if (testDataStorage == null) {
            testDataStorage = new TestDataStorage();
        }
        return testDataStorage;
    }

    /**
     * Adds the ID of a created entity to the storage for future deletion.
     *
     * @param apiEndpoint API endpoint where the entity was created
     * @param id          ID of the created entity
     */
    @Step("Add created entity for cleanup: {apiEndpoint} / {id}")
    public void addCreatedEntity(ApiEndpoint apiEndpoint, String id) {
        if (id != null) {
            createdEntitiesMap.computeIfAbsent(apiEndpoint, key -> new HashSet<>()).add(id);
        }
    }

    /**
     * Adds the entity to the storage using the name as locator.
     * Assumes that the entity has an ID retrievable by name lookup.
     *
     * @param apiEndpoint API endpoint where the entity was created
     * @param name        name of the created entity
     */
    public void addCreatedEntityByName(ApiEndpoint apiEndpoint, String name) {
        if (name != null) {
            var uncheckedBase = new UncheckedBase(RequestSpecs.superUserAuthSpec(), apiEndpoint);
            var response = uncheckedBase.read("name:" + name);
            var id = response.jsonPath().getString("buildType[0].id");
            addCreatedEntity(apiEndpoint, id);
        }
    }

    private String getEntityIdOrLocator(BaseModel model) {
        try {
            var idField = model.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            var idFieldValue = Objects.toString(idField.get(model), null);
            idField.setAccessible(false);
            return idFieldValue;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            try {
                var locatorField = model.getClass().getDeclaredField("locator");
                locatorField.setAccessible(true);
                var locatorFieldValue = Objects.toString(locatorField.get(model), null);
                locatorField.setAccessible(false);
                return locatorFieldValue;
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                throw new IllegalStateException("Unable to access 'id' or 'locator' field in " + model.getClass().getName(), e);
            }
        }
    }

    /**
     * Adds the created entity to the storage by extracting its ID or locator via reflection.
     *
     * @param apiEndpoint API endpoint where the entity was created
     * @param model       model instance to extract the ID or locator from
     */
    @Step("Add created entity by model for cleanup: {apiEndpoint}")
    public void addCreatedEntity(ApiEndpoint apiEndpoint, BaseModel model) {
        addCreatedEntity(apiEndpoint, getEntityIdOrLocator(model));
    }

    /**
     * Sends DELETE requests to remove all tracked entities from the server.
     * Clears the storage afterward.
     */
    @Step("Clean up all tracked test entities")
    public void cleanupTrackedEntities() {
        createdEntitiesMap.forEach(((endpoint, ids) -> ids.forEach(id -> new UncheckedBase(RequestSpecs.superUserAuthSpec(), endpoint).delete(id)))

        );

        createdEntitiesMap.clear();
    }
}
