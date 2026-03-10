package nl.bartvdhoven.triviamaster.exception;

/**
 * Exception thrown when a retrieved cache implementation
 * is not supported by the application.
 *
 * This occurs when the returned native cache is not a 
 * ConcurrentMap based implementation.
 */
public class UnsupportedCacheImplementationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnsupportedCacheImplementationException(Object nativeCache) {
        super("Unsupported cache implementation. Expected ConcurrentMap but got: " +
              (nativeCache == null ? "null" : nativeCache.getClass().getName()));
    }
}