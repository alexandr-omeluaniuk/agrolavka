package ss.martin.security.constants;

/**
 * Login fault codes.
 * @author alex
 */
public enum LoginFaultCode {
    
    WRONG_USERNAME("1", "Wrong username"),
    BAD_CREDENTIALS("2", "Wrong password"),
    DEACTIVATED("3", "User is deactivated"),
    SUBSCRIPTION_EXPIRED("4", "Subscription is expired. Please ask administrator");
    
    private final String code;
    
    private final String message;
    
    private LoginFaultCode(final String code, final String message) {
        this.code = code;
        this.message = message;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public String getMessage() {
        return this.message;
    }
}
