/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Core DAO API.
 * @author Alexandr Omeluaniuk
 */
public interface CoreDAO {
    /**
     * Create entity (subscription will be assigned automatically).
     * @param <T> entity class.
     * @param entity entity.
     * @return created entity.
     */
    <T> T create(T entity);
    /**
     * Update entity (subscription will be assigned automatically).
     * @param <T> entity class.
     * @param entity entity.
     * @return updated entity.
     */
    <T> T update(T entity);
    /**
     * Find entity by ID.
     * @param <T> entity type.
     * @param id entity ID.
     * @param cl entity class.
     * @return entity.
     * @throws Exception error.
     */
    <T> T findById(Serializable id, Class<T> cl) throws Exception;
    /**
     * Delete entity.
     * @param <T> entity type.
     * @param id entity ID.
     * @param cl entity class.
     * @throws Exception error.
     */
    <T> void delete(Serializable id, Class<T> cl) throws Exception;
    /**
     * Delete all entities.
     * @param <T> entity type.
     * @param cl entity class.
     * @throws Exception error.
     */
    <T> void deleteAll(Class<T> cl) throws Exception;
    /**
     * Mass creation.
     * @param <T> entity type.
     * @param list list of entities.
     * @throws Exception error.
     */
    <T> void massCreate(List<T> list) throws Exception; 
}
