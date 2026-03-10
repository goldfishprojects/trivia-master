package nl.bartvdhoven.triviamaster.exception;

/**
 * Exception thrown when a requested object with a specified key
 * can not be found in a cache.
 */
public class CachedObjectNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    public CachedObjectNotFoundException(Object key, String reason) {
        super("Cached object for key '" + key + "' is not found: " + reason);
    }
}
