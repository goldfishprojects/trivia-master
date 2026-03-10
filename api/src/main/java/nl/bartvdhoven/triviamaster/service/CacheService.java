package nl.bartvdhoven.triviamaster.service;

import java.util.List;

import nl.bartvdhoven.triviamaster.exception.CachedObjectNotFoundException;

/**
 * Service interface for interacting with application caches.
 *
 * This service provides a abstraction for storing and retrieving
 * objects from caches. It allows different cache implementations
 * to be used without changing the consuming services.
 *
 * Implementations are responsible for handling serialization and type
 * conversion when handling cached objects.
 */
public interface CacheService {
	
    /**
     * Retrieves all values stored in a specific cache.
     *
     * @param cacheName the name of the cache
     * @param type the expected type of the cached objects
     * @param <T> the type of objects stored in the cache
     * @return list of cached objects, or an empty list if the cache contains no data
     */
    <T> List<T> getAllValues(String cacheName, Class<T> type);
    
    /**
     * Retrieves a single value from the cache by its key.
     *
     * @param cacheName the name of the cache
     * @param key the key associated with the cached object
     * @param type the expected type of the cached object
     * @param <T> the type of the cached object
     * @return the cached object
     * @throws CachedObjectNotFoundException if the value is missing or has an unexpected type
     */
    <T> T get(String cacheName, Object key, Class<T> type);
    
    /**
     * Stores a value in the specified cache.
     *
     * If a value already exists for the given key it will be replaced.
     *
     * @param cacheName the name of the cache
     * @param key the key used to store the value
     * @param value the object to store in the cache
     */
    void put(String cacheName, Object key, Object value);
    
}
