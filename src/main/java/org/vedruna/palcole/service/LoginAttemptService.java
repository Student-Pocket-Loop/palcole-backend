package org.vedruna.palcole.service;

import java.util.Locale;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/** Servicio para controlar intentos fallidos de login y bloqueo temporal en caché. */
@Service
@AllArgsConstructor
public class LoginAttemptService {

  public static final String LOGIN_ATTEMPTS_CACHE = "loginAttempts";
  public static final int MAX_ATTEMPTS = 3;

  private final CacheManager cacheManager;

  /** Incrementa el contador de intentos fallidos para la clave indicada. */
  public void loginFailed(String key) {
    String normalizedKey = normalizeKey(key);
    if (normalizedKey == null) {
      return;
    }
    Cache cache = cacheManager.getCache(LOGIN_ATTEMPTS_CACHE);
    if (cache != null) {
      Integer attempts = cache.get(normalizedKey, Integer.class);
      cache.put(normalizedKey, (attempts == null ? 0 : attempts) + 1);
    }
  }

  /** Elimina el registro de intentos fallidos tras un login exitoso. */
  public void loginSucceeded(String key) {
    String normalizedKey = normalizeKey(key);
    if (normalizedKey == null) {
      return;
    }
    Cache cache = cacheManager.getCache(LOGIN_ATTEMPTS_CACHE);
    if (cache != null) {
      cache.evict(normalizedKey);
    }
  }

  /** Indica si la cuenta está bloqueada por alcanzar el máximo de intentos fallidos. */
  public boolean isBlocked(String key) {
    String normalizedKey = normalizeKey(key);
    if (normalizedKey == null) {
      return false;
    }
    Cache cache = cacheManager.getCache(LOGIN_ATTEMPTS_CACHE);
    if (cache == null) {
      return false;
    }
    Integer attempts = cache.get(normalizedKey, Integer.class);
    return attempts != null && attempts >= MAX_ATTEMPTS;
  }

  private String normalizeKey(String key) {
    if (key == null || key.isBlank()) {
      return null;
    }
    return key.trim().toLowerCase(Locale.ROOT);
  }
}
