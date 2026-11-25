package org.vedruna.palcole.controller.handler;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.JwtException;
import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.WebRequest;
import org.vedruna.palcole.domain.exception.users.TooManyLoginAttemptsException;

@ExtendWith(MockitoExtension.class)
class HandlerSecurityExControllerTest {

  @Mock private WebRequest request;

  @InjectMocks private HandlerSecurityExController handlerSecurityExController;

  @Test
  @DisplayName("Test handler handleAccessDenied Ok")
  void handleAccessDeniedOkTest() {

    // INPUT
    AccessDeniedException exInput = new AccessDeniedException("Access is denied");

    // EJECUCIÓN
    ResponseEntity<Object> res = handlerSecurityExController.handleAccessDenied(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.FORBIDDEN.value(), body.getStatus());
          assertEquals("AccessDeniedException: Access Denied", body.getTitle());
          assertEquals(
              "You do not have the required permissions or roles to access this resource.",
              body.getDetail());
          assertEquals(
              URI.create(
                  "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/access/AccessDeniedException.html"),
              body.getType());
          assertEquals("denied", body.getProperties().get("access"));
        });
  }

  @Test
  @DisplayName("Test handler handleBadCredentials Ok")
  void handleBadCredentialsOkTest() {

    // INPUT
    BadCredentialsException exInput = new BadCredentialsException("Bad credentials");

    // EJECUCIÓN
    ResponseEntity<Object> res = handlerSecurityExController.handleBadCredentials(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.UNAUTHORIZED, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.UNAUTHORIZED.value(), body.getStatus());
          assertEquals("BadCredentialsException: Invalid Credentials", body.getTitle());
          assertEquals(
              "The credentials provided are invalid. Please check your username and password.",
              body.getDetail());
          assertEquals(
              URI.create(
                  "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/BadCredentialsException.html"),
              body.getType());
          assertEquals("Invalid credentials", body.getProperties().get("error"));
        });
  }

  @Test
  @DisplayName("Test handler handleUsernameNotFound Ok")
  void handleUsernameNotFoundOkTest() {

    // INPUT
    UsernameNotFoundException exInput = new UsernameNotFoundException("Usuario no encontrado");

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerSecurityExController.handleUsernameNotFound(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.UNAUTHORIZED, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.UNAUTHORIZED.value(), body.getStatus());
          assertEquals("UsernameNotFoundException: User Not Found", body.getTitle());
          assertEquals("Usuario no encontrado", body.getDetail());
          assertEquals(
              URI.create(
                  "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/core/userdetails/UsernameNotFoundException.html"),
              body.getType());
          assertEquals("User not found", body.getProperties().get("error"));
        });
  }

  @Test
  @DisplayName("Test handler handleLocked Ok")
  void handleLockedOkTest() {

    // INPUT
    LockedException exInput = new LockedException("Account is locked");

    // EJECUCIÓN
    ResponseEntity<Object> res = handlerSecurityExController.handleLocked(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.UNAUTHORIZED, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.UNAUTHORIZED.value(), body.getStatus());
          assertEquals("LockedException: Account Locked", body.getTitle());
          assertEquals(
              "The user account is locked. Please contact support or an administrator to unlock your account.",
              body.getDetail());
          assertEquals(
              URI.create(
                  "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/LockedException.html"),
              body.getType());
          assertEquals("locked", body.getProperties().get("accountStatus"));
        });
  }

  @Test
  @DisplayName("Test handler handleDisabled Ok")
  void handleDisabledOkTest() {

    // INPUT
    DisabledException exInput = new DisabledException("Account is disabled");

    // EJECUCIÓN
    ResponseEntity<Object> res = handlerSecurityExController.handleDisabled(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.UNAUTHORIZED, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.UNAUTHORIZED.value(), body.getStatus());
          assertEquals("DisabledException: Account Disabled", body.getTitle());
          assertEquals(
              "The user account is disabled. Please verify or activate your account before attempting to log in.",
              body.getDetail());
          assertEquals(
              URI.create(
                  "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/DisabledException.html"),
              body.getType());
          assertEquals("disabled", body.getProperties().get("accountStatus"));
        });
  }

  @Test
  @DisplayName("Test handler handleAccountExpired Ok")
  void handleAccountExpiredOkTest() {

    // INPUT
    AccountExpiredException exInput = new AccountExpiredException("Account has expired");

    // EJECUCIÓN
    ResponseEntity<Object> res = handlerSecurityExController.handleAccountExpired(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.UNAUTHORIZED, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.UNAUTHORIZED.value(), body.getStatus());
          assertEquals("AccountExpiredException: Account Expired", body.getTitle());
          assertEquals(
              "The user account has expired. Please contact an administrator to renew your account.",
              body.getDetail());
          assertEquals(
              URI.create(
                  "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/AccountExpiredException.html"),
              body.getType());
          assertEquals("expired", body.getProperties().get("accountStatus"));
        });
  }

  @Test
  @DisplayName("Test handler handleCredentialsExpired Ok")
  void handleCredentialsExpiredOkTest() {

    // INPUT
    CredentialsExpiredException exInput =
        new CredentialsExpiredException("Credentials have expired");

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerSecurityExController.handleCredentialsExpired(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.UNAUTHORIZED, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.UNAUTHORIZED.value(), body.getStatus());
          assertEquals("CredentialsExpiredException: Credentials Expired", body.getTitle());
          assertEquals(
              "The user credentials (password) have expired. Please reset or update your password to proceed.",
              body.getDetail());
          assertEquals(
              URI.create(
                  "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/CredentialsExpiredException.html"),
              body.getType());
          assertEquals("expired", body.getProperties().get("credentialsStatus"));
        });
  }

  @Test
  @DisplayName("Test handler handleAccountStatus Ok")
  void handleAccountStatusOkTest() {

    // INPUT
    AccountStatusException exInput = mock(AccountStatusException.class);

    // MOCKS
    when(exInput.getMessage()).thenReturn("Estado de cuenta inválido");

    // EJECUCIÓN
    ResponseEntity<Object> res = handlerSecurityExController.handleAccountStatus(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.UNAUTHORIZED, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.UNAUTHORIZED.value(), body.getStatus());
          assertEquals("AccountStatusException: Account Status Error", body.getTitle());
          assertEquals("Estado de cuenta inválido", body.getDetail());
          assertEquals(
              URI.create(
                  "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/AccountStatusException.html"),
              body.getType());
        });
  }

  @Test
  @DisplayName("Test handler handleInsufficientAuthentication Ok")
  void handleInsufficientAuthenticationOkTest() {

    // INPUT
    InsufficientAuthenticationException exInput =
        new InsufficientAuthenticationException("Full authentication is required");

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerSecurityExController.handleInsufficientAuthentication(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.UNAUTHORIZED, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.UNAUTHORIZED.value(), body.getStatus());
          assertEquals(
              "InsufficientAuthenticationException: Insufficient Authentication", body.getTitle());
          assertEquals(
              "Full authentication is required to access this resource. Please provide a valid Bearer token.",
              body.getDetail());
          assertEquals(
              URI.create(
                  "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/InsufficientAuthenticationException.html"),
              body.getType());
          assertEquals("insufficient", body.getProperties().get("auth"));
        });
  }

  @Test
  @DisplayName("Test handler handleAuthenticationCredentialsNotFound Ok")
  void handleAuthenticationCredentialsNotFoundOkTest() {

    // INPUT
    AuthenticationCredentialsNotFoundException exInput =
        new AuthenticationCredentialsNotFoundException("Credentials not found");

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerSecurityExController.handleAuthenticationCredentialsNotFound(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.UNAUTHORIZED, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.UNAUTHORIZED.value(), body.getStatus());
          assertEquals(
              "AuthenticationCredentialsNotFoundException: Credentials Not Found", body.getTitle());
          assertEquals(
              "No authentication credentials were found in the security context to process this request.",
              body.getDetail());
          assertEquals(
              URI.create(
                  "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/AuthenticationCredentialsNotFoundException.html"),
              body.getType());
          assertEquals("credentials_not_found", body.getProperties().get("auth"));
        });
  }

  @Test
  @DisplayName("Test handler handleJwtException Ok")
  void handleJwtExceptionOkTest() {

    // INPUT
    JwtException exInput = new JwtException("Invalid JWT token");

    // EJECUCIÓN
    ResponseEntity<Object> res = handlerSecurityExController.handleJwtException(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.UNAUTHORIZED, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.UNAUTHORIZED.value(), body.getStatus());
          assertEquals("JwtException: Invalid or Expired Token", body.getTitle());
          assertEquals(
              "The provided JSON Web Token is invalid, malformed, or has expired. Please authenticate again.",
              body.getDetail());
          assertEquals(
              URI.create(
                  "https://javadoc.io/doc/io.jsonwebtoken/jjwt-api/latest/io/jsonwebtoken/JwtException.html"),
              body.getType());
          assertEquals("invalid_or_expired", body.getProperties().get("token"));
        });
  }

  @Test
  @DisplayName("Test handler handleAuthentication Ok")
  void handleAuthenticationOkTest() {

    // INPUT
    AuthenticationException exInput = mock(AuthenticationException.class);

    // MOCKS
    when(exInput.getMessage()).thenReturn("Fallo general de autenticación");

    // EJECUCIÓN
    ResponseEntity<Object> res = handlerSecurityExController.handleAuthentication(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.UNAUTHORIZED, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.UNAUTHORIZED.value(), body.getStatus());
          assertEquals("AuthenticationException: Authentication Failed", body.getTitle());
          assertEquals("Fallo general de autenticación", body.getDetail());
          assertEquals(
              URI.create(
                  "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/core/AuthenticationException.html"),
              body.getType());
          assertEquals("Authentication failed", body.getProperties().get("error"));
        });
  }

  @Test
  @DisplayName("Test handler handleTooManyLoginAttempts Ok")
  void handleTooManyLoginAttemptsOkTest() {

    // INPUT
    TooManyLoginAttemptsException exInput = mock(TooManyLoginAttemptsException.class);

    // MOCKS
    when(exInput.getMessage()).thenReturn("Has superado el máximo de intentos fallidos permitidos");

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerSecurityExController.handleTooManyLoginAttempts(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.TOO_MANY_REQUESTS, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.TOO_MANY_REQUESTS.value(), body.getStatus());
          assertEquals("TooManyLoginAttemptsException: Too Many Requests", body.getTitle());
          assertEquals("Has superado el máximo de intentos fallidos permitidos", body.getDetail());
          assertEquals("Account temporarily blocked", body.getProperties().get("error"));
        });
  }
}
