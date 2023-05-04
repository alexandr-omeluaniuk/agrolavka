package ss.entity.martin;

/**
 * Soft deleted entity.
 * @author ss
 */
public interface SoftDeleted {
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
