package ss.martin.security.configuration.custom;

/**
 * Login response.
 * @author ss
 */
public record LoginResponse(
        String jwt,
        String message
) {}
