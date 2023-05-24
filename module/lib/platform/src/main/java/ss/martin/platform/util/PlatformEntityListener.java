/*
 * The MIT License
 *
 * Copyright 2021 alex.
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
package ss.martin.platform.util;

import java.util.Set;
import ss.entity.martin.DataModel;

/**
 * Platform entity listener.
 * @author alex
 * @param <E> entity type.
 */
public interface PlatformEntityListener<E extends DataModel> {
    /**
     * Get listener target entity class.
     * @return entity class.
     */
    Class<E> entity();
    /**
     * Invoked before persist action.
     * @param entity entity.
     */
    default void prePersist(E entity) {
    }
    /**
     * Invoked after persist action.
     * @param entity entity.
     */
    default void postPersist(E entity) {
    }
    /**
     * Invoked before update action.
     * @param entity entity.
     */
    default void preUpdate(E entity) {
    }
    /**
     * Invoked after update action.
     * @param entity entity.
     */
    default void postUpdate(E entity) {
    }
    /**
     * Invoked before delete action.
     * @param ids entity IDs.
     */
    default void preDelete(Set<Long> ids) {
    }
    /**
     * Invoked after delete action.
     * @param ids entity IDs.
     */
    default void postDelete(Set<Long> ids) {
    }
}
