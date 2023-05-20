package ss.martin.security.model;

/**
 * Login request.
 * @author Alexandr Omeluaniuk
 */
public record LoginRequest(
        String username,
        String password
) {}
