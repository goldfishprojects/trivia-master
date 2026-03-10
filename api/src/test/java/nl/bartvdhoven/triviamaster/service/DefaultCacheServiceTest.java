package nl.bartvdhoven.triviamaster.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import nl.bartvdhoven.triviamaster.exception.CacheNotFoundException;
import nl.bartvdhoven.triviamaster.exception.CachedObjectNotFoundException;
import nl.bartvdhoven.triviamaster.exception.UnsupportedCacheImplementationException;
import nl.bartvdhoven.triviamaster.model.Question;

import static nl.bartvdhoven.triviamaster.test.TestDataFactory.createDefaultQuestion;

class DefaultCacheServiceTest {
	
    private CacheManager mockCacheManager;
    private Cache mockCache;
    private DefaultCacheService defaultCacheService;
    
    @BeforeEach
    void setUp() {
    	mockCacheManager = mock(CacheManager.class);
    	mockCache = mock(Cache.class);
    	defaultCacheService = new DefaultCacheService(mockCacheManager);
    }

    @Test
    void givenExistingCache_whenPut_thenStoreValueInCache() {
        when(mockCacheManager.getCache("questionsCache")).thenReturn(mockCache);

        defaultCacheService.put("questionsCache", 1, "value");

        verify(mockCache).put(1, "value");
    }
    
    @Test
    void givenMissingCache_whenPut_thenThrowCacheNotFoundException() {
        when(mockCacheManager.getCache("questionsCache")).thenReturn(null);

        CacheNotFoundException ex = assertThrows( CacheNotFoundException.class,
                () -> defaultCacheService.put("questionsCache", 1, "value")
        );

        assertEquals("Cache not found: questionsCache", ex.getMessage());
    }
    
    @Test
    void givenCachedQuestion_whenGet_thenReturnObject() {
    	
        Question question = createDefaultQuestion();
        Cache.ValueWrapper mockWrapper = mock(Cache.ValueWrapper.class);

        when(mockCacheManager.getCache("questionsCache")).thenReturn(mockCache);
        when(mockCache.get(question.getId())).thenReturn(mockWrapper);
        when(mockWrapper.get()).thenReturn(question);
        
        Question result = defaultCacheService.get("questionsCache", question.getId(), Question.class);

        assertNotNull(result);
        assertEquals(question, result);
    }
        
    @Test
    void givenMissingWrapper_whenGet_thenThrowQuestionNotFoundException() {
        when(mockCacheManager.getCache("questionsCache")).thenReturn(mockCache);
        when(mockCache.get(1)).thenReturn(null);

        CachedObjectNotFoundException ex = assertThrows( CachedObjectNotFoundException.class,
                () -> defaultCacheService.get("questionsCache", 1, String.class)
        );

        assertEquals("Cached object for key '1' is not found: Wrapper is null", ex.getMessage());
    }
    
    @Test
    void givenWrapperWithNullValue_whenGet_thenThrowQuestionNotFoundException() {
    	
        Cache.ValueWrapper mockWrapper = mock(Cache.ValueWrapper.class);

        when(mockCacheManager.getCache("questionsCache")).thenReturn(mockCache);
        when(mockCache.get(1)).thenReturn(mockWrapper);
        when(mockWrapper.get()).thenReturn(null);

        CachedObjectNotFoundException ex = assertThrows( CachedObjectNotFoundException.class,
                () -> defaultCacheService.get("questionsCache", 1, String.class)
        );

        assertEquals("Cached object for key '1' is not found: Value is null", ex.getMessage());
    }
    
    @Test
    void givenWrongType_whenGet_thenThrowQuestionNotFoundException() {
        Question question = createDefaultQuestion();
        Cache.ValueWrapper mockWrapper = mock(Cache.ValueWrapper.class);

        when(mockCacheManager.getCache("questionsCache")).thenReturn(mockCache);
        when(mockCache.get(question.getId())).thenReturn(mockWrapper);
        when(mockWrapper.get()).thenReturn(question);

        CachedObjectNotFoundException ex = assertThrows( CachedObjectNotFoundException.class,
                () -> defaultCacheService.get("questionsCache", question.getId(), String.class)
        );

        assertEquals("Cached object for key '" + question.getId() + "' is not found: Value is of wrong type", ex.getMessage());
    }

    @Test
    void givenFilledCache_whenGetAllValues_thenReturnObjectList() {
    	
        Question question1 = createDefaultQuestion();
        Question question2 = createDefaultQuestion();
        
        ConcurrentMap<Object, Object> nativeCache = new ConcurrentHashMap<>();
        nativeCache.put(question1.getId(), question1);
        nativeCache.put(question2.getId(), question2);
        
        Cache.ValueWrapper wrapper1 = mock(Cache.ValueWrapper.class);
        Cache.ValueWrapper wrapper2 = mock(Cache.ValueWrapper.class);

        when(mockCacheManager.getCache("questionsCache")).thenReturn(mockCache);
        when(mockCache.getNativeCache()).thenReturn(nativeCache);
        when(mockCache.get(question1.getId())).thenReturn(wrapper1);
        when(mockCache.get(question2.getId())).thenReturn(wrapper2);
        when(wrapper1.get()).thenReturn(question1);
        when(wrapper2.get()).thenReturn(question2);

        List<Question> result = defaultCacheService.getAllValues("questionsCache", Question.class);

        assertEquals(2, result.size());
        assertTrue(result.contains(question1));
        assertTrue(result.contains(question2));
    }
    
    @Test
    void givenNonConcurrentMapCache_whenGetAllValues_thenThrowUnsupportedCacheImplementationException() {

        Object nativeCache = new Object();

        when(mockCacheManager.getCache("questionsCache")).thenReturn(mockCache);
        when(mockCache.getNativeCache()).thenReturn(nativeCache);

        UnsupportedCacheImplementationException ex = assertThrows(
        		UnsupportedCacheImplementationException.class,
            () -> defaultCacheService.getAllValues("questionsCache", Question.class)
        );
        
        System.out.println(ex);

        assertEquals(
            "Unsupported cache implementation. Expected ConcurrentMap but got: " + nativeCache.getClass().getName(),
            ex.getMessage()
        );
    }    

}
