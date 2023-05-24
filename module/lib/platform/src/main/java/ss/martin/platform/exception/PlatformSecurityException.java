package ss.martin.platform.exception;

import ss.martin.base.exception.PlatformException;
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
    
    public EntityPermission getEntityPermission() {
        return entityPermission;
    }
    
    public void setEntityPermission(EntityPermission entityPermission) {
        this.entityPermission = entityPermission;
    }
    
    public Class<? extends DataModel> getDataModel() {
        return dataModel;
    }
    
    public void setDataModel(Class<? extends DataModel> dataModel) {
        this.dataModel = dataModel;
    }
}
