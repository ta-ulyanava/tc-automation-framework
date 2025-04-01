package com.example.teamcity.api.requests;

import com.example.teamcity.api.models.BaseModel;

/**
 * Interface defining basic CRUD operations for API requests.
 */
public interface CrudInterface {
    Object create(BaseModel model);

    /**
     * Reads an entity by its unique identifier.
     *
     * @param id the unique ID of the entity
     * @return the entity with the specified ID
     */
    Object read(String id);

    /**
     * Updates an existing entity with the given ID using the provided model.
     *
     * @param id    the unique ID of the entity
     * @param model the model with updated data
     * @return the updated entity
     */
    Object update(String id, BaseModel model);

    /**
     * Deletes an entity by its unique identifier.
     *
     * @param id the unique ID of the entity
     * @return the deleted entity or confirmation of deletion
     */
    Object delete(String id);
}
