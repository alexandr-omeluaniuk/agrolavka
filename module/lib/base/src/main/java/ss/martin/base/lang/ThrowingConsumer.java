package ss.martin.base.lang;

import java.util.function.Consumer;
import ss.martin.base.exception.PlatformException;

/**
 * Wrapper for checked exceptions.
 * @author alex
 */
public interface ThrowingConsumer<T> extends Consumer<T> {
    
    void acceptWithException(T param) throws Exception;
    
    @Override
    default void accept(T param) {
        try {
            acceptWithException(param);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new PlatformException(ex);
        }
    }
}
