package nl.bartvdhoven.triviamaster.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for application caching.
 *
 * Enables Spring caching mechanism and sets up an in memory cache
 * using ConcurrentMapCacheManager. This cache stores data in the
 * application memory.
 * 
 * Based on: https://docs.spring.io/spring-boot/reference/io/caching.html
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("questionsCache");
    }
}