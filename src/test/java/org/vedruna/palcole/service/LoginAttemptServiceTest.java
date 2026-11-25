package org.vedruna.palcole.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

@ExtendWith(MockitoExtension.class)
class LoginAttemptServiceTest {

  @Mock private CacheManager cacheManager;

  @Mock private Cache cache;

  @InjectMocks private LoginAttemptService loginAttemptService;

  @Test
  @DisplayName("Test service loginFailed primer intento Ok")
  void loginFailedPrimerIntentoOkTest() {

    // INPUT
    String keyInput = "  User@Example.com  ";
    String normalizedKey = "user@example.com";

    // MOCKS
    when(cacheManager.getCache(LoginAttemptService.LOGIN_ATTEMPTS_CACHE)).thenReturn(cache);
    when(cache.get(normalizedKey, Integer.class)).thenReturn(null);

    // EJECUCIÓN Y VALIDACIONES
    assertAll(
        () -> {
          assertDoesNotThrow(() -> loginAttemptService.loginFailed(keyInput));
          verify(cacheManager).getCache(LoginAttemptService.LOGIN_ATTEMPTS_CACHE);
          verify(cache).get(normalizedKey, Integer.class);
          verify(cache).put(normalizedKey, 1);
        });
  }

  @Test
  @DisplayName("Test service loginFailed incremento intento existente Ok")
  void loginFailedIncrementoIntentoOkTest() {

    // INPUT
    String keyInput = "user@example.com";

    // MOCKS
    when(cacheManager.getCache(LoginAttemptService.LOGIN_ATTEMPTS_CACHE)).thenReturn(cache);
    when(cache.get(keyInput, Integer.class)).thenReturn(2);

    // EJECUCIÓN Y VALIDACIONES
    assertAll(
        () -> {
          assertDoesNotThrow(() -> loginAttemptService.loginFailed(keyInput));
          verify(cacheManager).getCache(LoginAttemptService.LOGIN_ATTEMPTS_CACHE);
          verify(cache).get(keyInput, Integer.class);
          verify(cache).put(keyInput, 3);
        });
  }

  @Test
  @DisplayName("Test service loginFailed clave nula o en blanco")
  void loginFailedKeyNullOrBlankTest() {

    // INPUT
    String keyNull = null;
    String keyBlank = "   ";

    // EJECUCIÓN Y VALIDACIONES
    assertAll(
        () -> {
          assertDoesNotThrow(() -> loginAttemptService.loginFailed(keyNull));
          assertDoesNotThrow(() -> loginAttemptService.loginFailed(keyBlank));
          verify(cacheManager, never()).getCache(any());
        });
  }

  @Test
  @DisplayName("Test service loginFailed cache nula")
  void loginFailedCacheNullTest() {

    // INPUT
    String keyInput = "user@example.com";

    // MOCKS
    when(cacheManager.getCache(LoginAttemptService.LOGIN_ATTEMPTS_CACHE)).thenReturn(null);

    // EJECUCIÓN Y VALIDACIONES
    assertAll(
        () -> {
          assertDoesNotThrow(() -> loginAttemptService.loginFailed(keyInput));
          verify(cacheManager).getCache(LoginAttemptService.LOGIN_ATTEMPTS_CACHE);
        });
  }

  @Test
  @DisplayName("Test service loginSucceeded Ok")
  void loginSucceededOkTest() {

    // INPUT
    String keyInput = "  USER@Example.com ";
    String normalizedKey = "user@example.com";

    // MOCKS
    when(cacheManager.getCache(LoginAttemptService.LOGIN_ATTEMPTS_CACHE)).thenReturn(cache);

    // EJECUCIÓN Y VALIDACIONES
    assertAll(
        () -> {
          assertDoesNotThrow(() -> loginAttemptService.loginSucceeded(keyInput));
          verify(cacheManager).getCache(LoginAttemptService.LOGIN_ATTEMPTS_CACHE);
          verify(cache).evict(normalizedKey);
        });
  }

  @Test
  @DisplayName("Test service loginSucceeded clave nula o en blanco")
  void loginSucceededKeyNullOrBlankTest() {

    // INPUT
    String keyNull = null;
    String keyBlank = "   ";

    // EJECUCIÓN Y VALIDACIONES
    assertAll(
        () -> {
          assertDoesNotThrow(() -> loginAttemptService.loginSucceeded(keyNull));
          assertDoesNotThrow(() -> loginAttemptService.loginSucceeded(keyBlank));
          verify(cacheManager, never()).getCache(any());
        });
  }

  @Test
  @DisplayName("Test service loginSucceeded cache nula")
  void loginSucceededCacheNullTest() {

    // INPUT
    String keyInput = "user@example.com";

    // MOCKS
    when(cacheManager.getCache(LoginAttemptService.LOGIN_ATTEMPTS_CACHE)).thenReturn(null);

    // EJECUCIÓN Y VALIDACIONES
    assertAll(
        () -> {
          assertDoesNotThrow(() -> loginAttemptService.loginSucceeded(keyInput));
          verify(cacheManager).getCache(LoginAttemptService.LOGIN_ATTEMPTS_CACHE);
        });
  }

  @Test
  @DisplayName("Test service isBlocked True")
  void isBlockedTrueTest() {

    // INPUT
    String keyInput = "  User@Example.com ";
    String normalizedKey = "user@example.com";

    // MOCKS
    when(cacheManager.getCache(LoginAttemptService.LOGIN_ATTEMPTS_CACHE)).thenReturn(cache);
    when(cache.get(normalizedKey, Integer.class)).thenReturn(LoginAttemptService.MAX_ATTEMPTS);

    // EJECUCIÓN
    boolean res = loginAttemptService.isBlocked(keyInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertTrue(res);
          verify(cacheManager).getCache(LoginAttemptService.LOGIN_ATTEMPTS_CACHE);
          verify(cache).get(normalizedKey, Integer.class);
        });
  }

  @Test
  @DisplayName("Test service isBlocked False por intentos insuficientes")
  void isBlockedFalsePorIntentosInsuficientesTest() {

    // INPUT
    String keyInput = "user@example.com";

    // MOCKS
    when(cacheManager.getCache(LoginAttemptService.LOGIN_ATTEMPTS_CACHE)).thenReturn(cache);
    when(cache.get(keyInput, Integer.class)).thenReturn(LoginAttemptService.MAX_ATTEMPTS - 1);

    // EJECUCIÓN
    boolean res = loginAttemptService.isBlocked(keyInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertFalse(res);
          verify(cacheManager).getCache(LoginAttemptService.LOGIN_ATTEMPTS_CACHE);
          verify(cache).get(keyInput, Integer.class);
        });
  }

  @Test
  @DisplayName("Test service isBlocked False cuando no hay intentos previos")
  void isBlockedFalseSinIntentosTest() {

    // INPUT
    String keyInput = "user@example.com";

    // MOCKS
    when(cacheManager.getCache(LoginAttemptService.LOGIN_ATTEMPTS_CACHE)).thenReturn(cache);
    when(cache.get(keyInput, Integer.class)).thenReturn(null);

    // EJECUCIÓN
    boolean res = loginAttemptService.isBlocked(keyInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertFalse(res);
          verify(cacheManager).getCache(LoginAttemptService.LOGIN_ATTEMPTS_CACHE);
          verify(cache).get(keyInput, Integer.class);
        });
  }

  @Test
  @DisplayName("Test service isBlocked False clave nula o en blanco")
  void isBlockedFalseKeyNullOrBlankTest() {

    // INPUT
    String keyNull = null;
    String keyBlank = "   ";

    // EJECUCIÓN
    boolean resNull = loginAttemptService.isBlocked(keyNull);
    boolean resBlank = loginAttemptService.isBlocked(keyBlank);

    // VALIDACIONES
    assertAll(
        () -> {
          assertFalse(resNull);
          assertFalse(resBlank);
          verify(cacheManager, never()).getCache(any());
        });
  }

  @Test
  @DisplayName("Test service isBlocked False cache nula")
  void isBlockedFalseCacheNullTest() {

    // INPUT
    String keyInput = "user@example.com";

    // MOCKS
    when(cacheManager.getCache(LoginAttemptService.LOGIN_ATTEMPTS_CACHE)).thenReturn(null);

    // EJECUCIÓN
    boolean res = loginAttemptService.isBlocked(keyInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertFalse(res);
          verify(cacheManager).getCache(LoginAttemptService.LOGIN_ATTEMPTS_CACHE);
        });
  }
}
