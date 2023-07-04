package ss.martin.security.api;

/**
 * Alert service.
 * @author alex
 */
public interface AlertService {
    
    /**
     * Send alert.
     * @param message message.
     * @param ex exception.
     */
    void sendAlert(String message, Exception ex);
    
    /**
     * Send alert.
     * @param message message.
     */
    default void sendAlert(String message) {
        sendAlert(message, null);
    }
    
    /**
     * Send alert.
     * @param ex exception.
     */
    default void sendAlert(Exception ex) {
        sendAlert(null, ex);
    }
}
