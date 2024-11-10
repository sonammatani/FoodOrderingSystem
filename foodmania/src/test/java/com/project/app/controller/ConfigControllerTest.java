package com.project.app.controller;

import com.project.app.service.RedisManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ConfigControllerTest {

    @Mock
    private RedisManagementService redisManagementService;

    @InjectMocks
    private ConfigController configController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); 
    }

    @Test
    void testGetValue() {
        String key = "testKey";
        String expectedValue = "testValue";
        when(redisManagementService.getValue(key)).thenReturn(expectedValue);
        String actualValue = configController.getValue(key);

        assertEquals(expectedValue, actualValue);
        verify(redisManagementService, times(1)).getValue(key); 
    }

    @Test
    void testRefreshCache() {
        String key = "testKey";
        String response = configController.refreshCache(key);

        assertEquals("Cache refreshed for key: " + key, response);
        verify(redisManagementService, times(1)).refreshCache(key); 
    }

    @Test
    void testUpdateValue() {
        String key = "testKey";
        String value = "newValue";
        String response = configController.updateValue(key, value);
        
        assertEquals("Value updated for key: " + key, response);
        verify(redisManagementService, times(1)).updateValue(key, value);
    }

    @Test
    void testDeleteKey() {
        String key = "testKey";
        configController.deleteKey(key);

        verify(redisManagementService, times(1)).deleteCacheByKey(key);
    }
}
