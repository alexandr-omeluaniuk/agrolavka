package ss.entity.martin;

/**
 * Soft deleted entity.
 * @author ss
 */
public interface SoftDeleted {
    /** 'Active' field name. */
    static final String ACTIVE_FIELD_NAME = "active";
    
    /**
     * Get active flag.
     * @return the active
     */
    public boolean isActive();
    
    /**
     * Set active flag.
     * @param active the active to set
     */
    public void setActive(boolean active);
}
