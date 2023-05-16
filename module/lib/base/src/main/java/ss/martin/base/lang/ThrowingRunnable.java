package ss.martin.base.lang;

import ss.martin.base.exception.PlatformException;

/**
 * Wrapper for checked exceptions.
 * @author alex
 */
@FunctionalInterface
public interface ThrowingRunnable extends Runnable {
    
    void runWithException() throws Exception;
    
    @Override
    default void run() {
        try {
            runWithException();
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new PlatformException(ex);
        }
    }
}
