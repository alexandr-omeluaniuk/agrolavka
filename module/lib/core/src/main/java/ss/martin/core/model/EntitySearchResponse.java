package ss.martin.core.model;

import java.util.List;

/**
 * Entity search response.
 *
 * @author ss
 * @param <T> entity type.
 */
public record EntitySearchResponse<T>(
        long total,
        List<T> data
) {
    
    public EntitySearchResponse(final List<T> data) {
        this(0, data);
    }
}
