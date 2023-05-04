package ss.martin.core.model;

/**
 * Entity search request.
 *
 * @author ss
 */
public record EntitySearchRequest(
    Integer page,
    Integer pageSize,
    String order,
    String orderBy,
    boolean ignoreCount,
    boolean showDeactivated
) {

    public EntitySearchRequest(final Integer page, final Integer pageSize) {
        this(page, pageSize, null, null, false, false);
    }
    
    public EntitySearchRequest(final Integer page, final Integer pageSize, final String order, final String orderBy) {
        this(page, pageSize, order, orderBy, false, false);
    }
}
