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
package ss.martin.platform.exception;

import ss.martin.platform.constants.EntityPermission;
import ss.entity.martin.DataModel;

/**
 * Platform security exception.
 * @author ss
 */
public class PlatformSecurityException extends PlatformException {
    /** Entity permission. */
    private EntityPermission entityPermission;
    /** Data model. */
    private Class<? extends DataModel> dataModel;
    /**
     * Constructor.
     * @param msg message.
     */
    public PlatformSecurityException(String msg) {
        super(msg);
    }
    /**
     * Platform security exception.
     * @param entityPermission entity permission.
     * @param dataModel entity class.
     */
    public PlatformSecurityException(EntityPermission entityPermission, Class<? extends DataModel> dataModel) {
        super("Access denied to '" + dataModel.getName() + "', operation '" + entityPermission.name() + "'");
        this.entityPermission = entityPermission;
        this.dataModel = dataModel;
    }
    /**
     * @return the entityPermission
     */
    public EntityPermission getEntityPermission() {
        return entityPermission;
    }
    /**
     * @param entityPermission the entityPermission to set
     */
    public void setEntityPermission(EntityPermission entityPermission) {
        this.entityPermission = entityPermission;
    }
    /**
     * @return the dataModel
     */
    public Class<? extends DataModel> getDataModel() {
        return dataModel;
    }
    /**
     * @param dataModel the dataModel to set
     */
    public void setDataModel(Class<? extends DataModel> dataModel) {
        this.dataModel = dataModel;
    }
}
