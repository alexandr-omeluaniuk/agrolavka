package ss.martin.security.exception;

import ss.martin.base.exception.PlatformException;

/**
 * Registration user exception.
 * @author ss
 */
public class RegistrationUserException extends PlatformException {
    /** Duplicate user. */
    public static final String CODE_DUPLICATE_USER = "DUPLICATE_USER";
    
    /**
     * Constructor.
     * @param errorCode error code, uses translations on the client-side. 
     */
    public RegistrationUserException(String errorCode) {
        super(errorCode);
    }
}
