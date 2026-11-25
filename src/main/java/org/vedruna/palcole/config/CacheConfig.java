package org.vedruna.palcole.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.cache.autoconfigure.CacheManagerCustomizer;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configuración personalizada de cachés Caffeine para casos con TTL específico. */
@Configuration
public class CacheConfig {

  public static final String LOGIN_ATTEMPTS_CACHE = "loginAttempts";

  @Bean
  CacheManagerCustomizer<CaffeineCacheManager> loginAttemptsCacheCustomizer() {
    return cacheManager ->
        cacheManager.registerCustomCache(
            LOGIN_ATTEMPTS_CACHE,
            Caffeine.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).maximumSize(500).build());
  }
}
