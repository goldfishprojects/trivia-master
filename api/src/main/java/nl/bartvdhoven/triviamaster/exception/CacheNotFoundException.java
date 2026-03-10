package nl.bartvdhoven.triviamaster.exception;

/**
 * Exception thrown when a requested cache can not be found.
 */
public class CacheNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public CacheNotFoundException(String cacheName) {
        super("Cache not found: " + cacheName);
    }
    
}
