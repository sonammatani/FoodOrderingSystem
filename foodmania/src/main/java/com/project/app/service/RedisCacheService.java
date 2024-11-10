package com.project.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisCacheService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public String getFromCache(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void putInCache(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void evictFromCache(String key) {
        redisTemplate.delete(key);
    }

    public void evictAllFromCache() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }
}

