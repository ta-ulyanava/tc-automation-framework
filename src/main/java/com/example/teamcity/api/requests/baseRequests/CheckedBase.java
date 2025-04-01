package com.example.teamcity.api.requests.baseRequests;

import com.example.teamcity.api.enums.ApiEndpoint;
import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import java.util.List;

/**
 * CheckedBase is a wrapper around UncheckedBase that adds validation of API responses.
 * It also stores created entities for further test management.
 *
 * @param <T> the type of the entity extending BaseModel
 */
public final class CheckedBase<T extends BaseModel> extends Request implements CrudInterface {
    private final UncheckedBase uncheckedBase;

    public CheckedBase(RequestSpecification spec, ApiEndpoint apiEndpoint) {
        super(spec, apiEndpoint);
        this.uncheckedBase = new UncheckedBase(spec, apiEndpoint);
    }

    private void validateResponse(Response response, String locator) {
        response.then().assertThat().statusCode(HttpStatus.SC_OK);
        if (response.getBody().asString().isEmpty()) {
            throw new IllegalStateException("Empty response for locator '%s'".formatted(locator));
        }
    }

    private T extractEntity(Response response) {
        return response.as((Class<T>) apiEndpoint.getModelClass());
    }

    private List<T> extractEntityList(Response response) {
        return response.jsonPath().getList("project", (Class<T>) apiEndpoint.getModelClass());
    }

    /**
     * Creates a new entity using the API and stores it in TestDataStorage.
     *
     * @param model the entity to be created
     * @return the API response
     */
    @Step("Create entity via API: {model}")
    @Override
    public Response create(BaseModel model) {
        Response response = uncheckedBase.create(model);
        validateResponse(response, model.toString());
        BaseModel createdModel = response.getBody().as(apiEndpoint.getModelClass());
        TestDataStorage.getStorageInstance().addCreatedEntity(apiEndpoint, createdModel);
        return response;
    }

    /**
     * Reads the entity with the specified ID.
     *
     * @param id the ID of the entity
     * @return the entity
     */
    @Step("Read entity by ID: {id}")
    @Override
    public T read(String id) {
        Response response = uncheckedBase.read(id);
        validateResponse(response, id);
        return extractEntity(response);
    }

    /**
     * Updates the entity with the specified ID.
     *
     * @param id    the ID of the entity to be updated
     * @param model the new data
     * @return the updated entity
     */
    @Step("Update entity ID: {id} with new model")
    @Override
    public T update(String id, BaseModel model) {
        Response response = uncheckedBase.update(id, model);
        validateResponse(response, id);
        return extractEntity(response);
    }

    /**
     * Deletes the entity with the specified ID.
     *
     * @param id the ID of the entity to delete
     * @return the raw response as string
     */
    @Step("Delete entity by ID: {id}")
    @Override
    public Object delete(String id) {
        Response response = uncheckedBase.delete(id);
        validateResponse(response, id);
        return response.asString();
    }
}
