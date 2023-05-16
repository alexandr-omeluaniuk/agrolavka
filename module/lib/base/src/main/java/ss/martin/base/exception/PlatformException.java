package ss.martin.base.exception;

/**
 * Application-specific exception for common cases.
 * @author ss
 */
public class PlatformException extends RuntimeException {
    /**
     * Constructor.
     * @param msg error message.
     * @param th throwable.
     */
    public PlatformException(String msg, Throwable th) {
        super(msg, th);
    }
    
    /**
     * Constructor.
     * @param msg error message.
     */
    public PlatformException(String msg) {
        super(msg);
    }
    
    public PlatformException(Throwable th) {
        super(th);
    }
}
