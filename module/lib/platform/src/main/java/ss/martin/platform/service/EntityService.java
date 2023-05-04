/*
 * The MIT License
 *
 * Copyright 2020 ss.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ss.martin.platform.service;

import java.util.Set;
import ss.entity.martin.DataModel;
import ss.martin.core.model.EntitySearchRequest;
import ss.martin.core.model.EntitySearchResponse;

/**
 * Entity service.
 * @author ss
 */
public interface EntityService {
    /**
     * Search entities.
     * @param clazz entity class.
     * @param searchRequest search request.
     * @return search response.
     * @throws Exception error.
     */
    EntitySearchResponse list(Class<? extends DataModel> clazz, EntitySearchRequest searchRequest)
            throws Exception;
    /**
     * Create entity.
     * @param <T> entity type.
     * @param entity entity.
     * @return entity.
     * @throws Exception error.
     */
    <T extends DataModel> T create(T entity) throws Exception;
    /**
     * Update entity.
     * @param <T> entity type.
     * @param entity entity.
     * @return updated entity.
     * @throws Exception error.
     */
    <T extends DataModel> T update(T entity) throws Exception;
    /**
     * Delete entities.
     * @param <T> entity type.
     * @param ids set of IDs.
     * @param cl entity class.
     * @throws Exception error.
     */
    <T extends DataModel> void delete(Set<Long> ids, Class<T> cl) throws Exception;
    /**
     * Find entity by ID.
     * @param <T> entity type.
     * @param id entity ID.
     * @param cl entity class.
     * @return entity.
     * @throws Exception error.
     */
    <T extends DataModel> T get(Long id, Class<T> cl) throws Exception;
}
