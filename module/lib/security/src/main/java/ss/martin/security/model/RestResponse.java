package ss.martin.security.model;

/**
 * REST response.
 * @author ss
 */
public record RestResponse(
        boolean success,
        String message,
        String code,
        String details,
        String stacktrace
) {
    
    public RestResponse() {
        this(true, null, null, null, null);
    }
    
    public RestResponse(boolean success, String message) {
        this(success, message, null, null, null);
    }
    
    public RestResponse(final boolean success, final String message, final String code) {
        this(success, message, code, null, null);
    }
}
