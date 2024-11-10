package com.project.app.service;

import com.project.app.model.Config;
import com.project.app.repository.ConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class RedisManagementService {

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private RedisCacheService cacheService;

    public String getValue(String key) {
        // First check in the cache
        String value = cacheService.getFromCache(key);
        if (value != null) {
            return value;
        }

        // If not in cache, fetch from DB and cache it
        Optional<Config> keyValue = configRepository.findByKey(key);
        if (keyValue.isPresent()) {
            cacheService.putInCache(key, keyValue.get().getValue());
            return keyValue.get().getValue();
        }
        return null;
    }

    public void refreshCache(String key) {
        Optional<Config> keyValue = configRepository.findByKey(key);
        keyValue.ifPresent(kv -> cacheService.putInCache(key, kv.getValue()));
    }

    @Transactional
    public void updateValue(String key, String value) {
        // Update in database
        Config config = configRepository.findByKey(key).orElse(new Config());
        config.setKey(key);
        config.setValue(value);
        configRepository.save(config);

        // Update in cache
        cacheService.putInCache(key, value);
    }

    public void deleteCacheByKey(String key) {
        log.info("Key is present : {}" , getValue(key));
        cacheService.evictFromCache(key);
    }
}
