package ss.martin.base.lang;

import java.util.function.Supplier;
import ss.martin.base.exception.PlatformException;

/**
 * Wrapper for checked exceptions.
 * @author alex
 */
@FunctionalInterface
public interface ThrowingSupplier<T> extends Supplier<T> {
    
    T getWithException() throws Exception;
    
    @Override
    default T get() {
        try {
            return getWithException();
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new PlatformException(ex);
        }
    }
}
