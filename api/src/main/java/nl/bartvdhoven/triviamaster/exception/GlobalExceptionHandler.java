package nl.bartvdhoven.triviamaster.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the application.
 *
 * Converts internal exceptions into standardized HTTP responses
 * that can be returned to the client.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
 
    @ExceptionHandler(CachedObjectNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCachedObjectNotFound(CachedObjectNotFoundException ex) {
    	
    	log.warn("Cached object not found: {}", ex.getMessage());
    	
        Map<String, String> body = new HashMap<>();
        body.put("code", "CACHED_OBJECT_NOT_FOUND");
        body.put("message", "One or more cached objects could not be found.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
    
    @ExceptionHandler(CacheNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCacheNotFound(CacheNotFoundException ex) {
    	
    	log.warn("Cached not found: {}", ex.getMessage());
    	
        Map<String, String> body = new HashMap<>();
        body.put("code", "CACHE_NOT_FOUND");
        body.put("message", "The requested cache could not be found.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
    
    @ExceptionHandler(UnsupportedCacheImplementationException.class)
    public ResponseEntity<Map<String, String>> handleUnsupportedCache(UnsupportedCacheImplementationException ex) {
    	
    	log.warn("Unsupported cache implementation: {}", ex.getMessage());

        Map<String, String> body = new HashMap<>();
        body.put("code", "CACHE_IMPLEMENTATION_ERROR");
        body.put("message", "Cache configuration is invalid.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}
