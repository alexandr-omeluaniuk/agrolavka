package ss.martin.base.lang;

import java.util.function.Function;
import ss.martin.base.exception.PlatformException;

/**
 * Wrapper for checked exceptions.
 * @author alex
 */
public interface ThrowingFunction<T, R> extends Function<T, R> {
    
    R applyWithException(T param) throws Exception;
    
    @Override
    default R apply(final T param) {
        try {
            return applyWithException(param);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new PlatformException(ex);
        }
    }
}
