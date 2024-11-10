package com.project.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RedisCacheServiceTest {

    @InjectMocks
    private RedisCacheService redisCacheService;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testGetFromCache() {
        String key = "testKey";
        String value = "testValue";

        when(valueOperations.get(key)).thenReturn(value);

        String result = redisCacheService.getFromCache(key);

        assertEquals(value, result);
        verify(redisTemplate, times(1)).opsForValue();
        verify(valueOperations, times(1)).get(key);
    }

    @Test
    void testPutInCache() {
        String key = "testKey";
        String value = "testValue";

        redisCacheService.putInCache(key, value);

        verify(redisTemplate, times(1)).opsForValue();
        verify(valueOperations, times(1)).set(key, value);
    }

    @Test
    void testEvictFromCache() {
        String key = "testKey";
        redisCacheService.evictFromCache(key);
        verify(redisTemplate, times(1)).delete(key);
    }
}
