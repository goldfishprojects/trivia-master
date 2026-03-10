package nl.bartvdhoven.triviamaster.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import nl.bartvdhoven.triviamaster.exception.CacheNotFoundException;
import nl.bartvdhoven.triviamaster.exception.CachedObjectNotFoundException;
import nl.bartvdhoven.triviamaster.exception.UnsupportedCacheImplementationException;

/**
 * Default implementation of {@link CacheService} based on Spring's {@link CacheManager}.
 */
@Service
public class DefaultCacheService implements CacheService{
	
	private static final Logger log = LoggerFactory.getLogger(DefaultCacheService.class);

	private final CacheManager cacheManager;

	public DefaultCacheService(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	/**
	 * Retrieves a cache by name.
	 *
	 * @param cacheName the name of the cache
	 * @return the requested cache
	 * @throws CacheNotFoundException if the cache does not exist
	 */
	private Cache getCache(String cacheName) {
		
		log.debug("Retrieving cache '{}'", cacheName);
		
	    Cache cache = cacheManager.getCache(cacheName);
	    if (cache == null) {
	        throw new CacheNotFoundException(cacheName);
	    }
	    return cache;
	}
	
	/**
	 * Retrieves a value from the cache and verifies that it matches the expected type.
	 *
	 * @param cache the cache to read from
	 * @param key the key of the cached value
	 * @param type the expected type
	 * @param <T> the expected value type
	 * @return the cached value cast to the requested type
	 * @throws CachedObjectNotFoundException if the value is missing or has an unexpected type
	 */
	private <T> T getTypedValue(Cache cache, Object key, Class<T> type) {
		
		log.debug("Retrieving typed value for key='{}' as type={}", key, type.getSimpleName());
		
	    Cache.ValueWrapper wrapper = cache.get(key);
	    if (wrapper == null) {
	        throw new CachedObjectNotFoundException(key, "Wrapper is null");
	    }

	    Object value = wrapper.get();
	    if (value == null) {
	        throw new CachedObjectNotFoundException(key, "Value is null");
	    }

	    if (!type.isInstance(value)) {
	        throw new CachedObjectNotFoundException(key, "Value is of wrong type");
	    }

	    return type.cast(value);
	}
	
    @Override
    public void put(String cacheName, Object key, Object value) {    
    	
    	log.debug("Storing value in cache '{}' with key '{}'", cacheName, key);
    	
        Cache cache = getCache(cacheName);        
        cache.put(key, value);
        
    }	

	@Override
	public <T> T get(String cacheName, Object key, Class<T> type) {
		
		log.debug("Fetching value from cache '{}' with key '{}'", cacheName, key);
		
		Cache cache = getCache(cacheName);		
	    return getTypedValue(cache, key, type);
	    
	}	
	
	@Override
	public <T> List<T> getAllValues(String cacheName, Class<T> type) {
		
		log.debug("Retrieving all values from cache '{}' as type={}", cacheName, type.getSimpleName());
		 
		Cache cache = getCache(cacheName);

	    Object nativeCache = cache.getNativeCache();
	    
	    // Iteration over all cached entries is only supported for ConcurrentMap based caches.
	    if (!(nativeCache instanceof ConcurrentMap<?, ?> map)) {
	    	throw new UnsupportedCacheImplementationException(nativeCache);
	    }
	    
	    List<T> result = new ArrayList<>();

	    for (Object key : map.keySet()) {
	    	
	        result.add(getTypedValue(cache, key, type));
	    }
	    
	    log.debug("Retrieved {} values from cache '{}'", result.size(), cacheName);

	    return result;    	
	} 



}
