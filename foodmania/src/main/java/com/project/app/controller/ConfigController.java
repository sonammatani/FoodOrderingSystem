package com.project.app.controller;

import com.project.app.service.RedisManagementService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private RedisManagementService redisManagementService;

    // 1. Get Value by Key
    @GetMapping("/{key}")
    public String getValue(@PathVariable String key) {
        return redisManagementService.getValue(key);
    }

    // 2. Refresh Cache for a Key
    @PutMapping("/refresh/{key}")
    public String refreshCache(@PathVariable String key) {
        redisManagementService.refreshCache(key);
        return "Cache refreshed for key: " + key;
    }

    // 3. Update Value for a Key
    @PutMapping("/{key}")
    public String updateValue(@PathVariable String key, @RequestBody String value) {
        redisManagementService.updateValue(key, value);
        return "Value updated for key: " + key;
    }

    //delete key from the cache
    @DeleteMapping("/delete/{key}")
    public void deleteKey(@PathVariable String key) {
        redisManagementService.deleteCacheByKey(key);
    }

    @DeleteMapping("/clear")
    public String clearAllCache() {
        redisManagementService.refreshAllCache();
        return "All cache cleared successfully";
    }
}

