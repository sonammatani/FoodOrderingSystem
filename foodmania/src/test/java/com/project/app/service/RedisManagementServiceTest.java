package com.project.app.service;

import com.project.app.model.Config;
import com.project.app.repository.ConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RedisManagementServiceTest {

    @InjectMocks
    private RedisManagementService redisManagementService;

    @Mock
    private ConfigRepository configRepository;

    @Mock
    private RedisCacheService cacheService;

    private Config config;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        config = new Config();
        config.setKey("testKey");
        config.setValue("testValue");
    }

    @Test
    void testGetValueFromCache() {
        String key = "testKey";
        when(cacheService.getFromCache(key)).thenReturn("testValue");

        String result = redisManagementService.getValue(key);

        assertEquals("testValue", result);
        verify(cacheService, times(1)).getFromCache(key);
    }

    @Test
    void testGetValueFromDatabaseAndCache() {
        String key = "testKey";
        when(cacheService.getFromCache(key)).thenReturn(null);
        when(configRepository.findByKey(key)).thenReturn(Optional.of(config));

        String result = redisManagementService.getValue(key);

        assertEquals("testValue", result);
        verify(cacheService, times(1)).putInCache(key, "testValue");
    }

    @Test
    void testRefreshCache() {
        String key = "testKey";
        when(configRepository.findByKey(key)).thenReturn(Optional.of(config));
        redisManagementService.refreshCache(key);

        verify(cacheService, times(1)).putInCache(key, "testValue");
    }

    @Test
    void testUpdateValue() {
        String key = "testKey";
        String newValue = "newValue";
        redisManagementService.updateValue(key, newValue);

        verify(configRepository, times(1)).save(any(Config.class));
        verify(cacheService, times(1)).putInCache(key, newValue);
    }

    @Test
    void testDeleteCacheByKey() {
        String key = "testKey";
        when(cacheService.getFromCache(key)).thenReturn("testValue");
        redisManagementService.deleteCacheByKey(key);

        verify(cacheService, times(1)).evictFromCache(key);
    }
}
