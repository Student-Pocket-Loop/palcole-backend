package org.vedruna.palcole.controller.handler;

import io.jsonwebtoken.JwtException;
import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.vedruna.palcole.domain.exception.users.TooManyLoginAttemptsException;

/**
 * Controlador de Asesoramiento REST (Security Exception Handler) para gestionar de forma
 * centralizada las excepciones de Spring Security y seguridad JWT lanzadas por la aplicación,
 * transformándolas en respuestas HTTP estandarizadas basadas en la especificación RFC 7807
 * (ProblemDetail).
 *
 * <p>{@code @Order(Ordered.HIGHEST_PRECEDENCE)}: Asegura que los manejadores de seguridad tengan
 * prioridad máxima de resolución frente a manejadores genéricos de excepciones.
 */
@AllArgsConstructor
@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HandlerSecurityExController {

  @ExceptionHandler(AccessDeniedException.class)
  protected ResponseEntity<Object> handleAccessDenied(
      AccessDeniedException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.FORBIDDEN,
            "You do not have the required permissions or roles to access this resource.");
    problemDetail.setTitle("AccessDeniedException: Access Denied");
    problemDetail.setType(
        URI.create(
            "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/access/AccessDeniedException.html"));
    problemDetail.setProperty("access", "denied");
    log.warn("Access Denied Exception (403 Forbidden): {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problemDetail);
  }

  @ExceptionHandler(BadCredentialsException.class)
  protected ResponseEntity<Object> handleBadCredentials(
      BadCredentialsException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED,
            "The credentials provided are invalid. Please check your username and password.");
    problemDetail.setTitle("BadCredentialsException: Invalid Credentials");
    problemDetail.setType(
        URI.create(
            "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/BadCredentialsException.html"));
    problemDetail.setProperty("error", "Invalid credentials");
    log.warn("Bad Credentials Exception (401 Unauthorized): {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  protected ResponseEntity<Object> handleUsernameNotFound(
      UsernameNotFoundException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED,
            ex.getMessage() != null && !ex.getMessage().trim().isEmpty()
                ? ex.getMessage()
                : "The requested user could not be found.");
    problemDetail.setTitle("UsernameNotFoundException: User Not Found");
    problemDetail.setType(
        URI.create(
            "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/core/userdetails/UsernameNotFoundException.html"));
    problemDetail.setProperty("error", "User not found");
    log.warn("Username Not Found Exception (401 Unauthorized): {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
  }

  @ExceptionHandler(LockedException.class)
  protected ResponseEntity<Object> handleLocked(LockedException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED,
            "The user account is locked. Please contact support or an administrator to unlock your account.");
    problemDetail.setTitle("LockedException: Account Locked");
    problemDetail.setType(
        URI.create(
            "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/LockedException.html"));
    problemDetail.setProperty("accountStatus", "locked");
    log.warn("Account Locked Exception (401 Unauthorized): {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
  }

  @ExceptionHandler(DisabledException.class)
  protected ResponseEntity<Object> handleDisabled(DisabledException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED,
            "The user account is disabled. Please verify or activate your account before attempting to log in.");
    problemDetail.setTitle("DisabledException: Account Disabled");
    problemDetail.setType(
        URI.create(
            "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/DisabledException.html"));
    problemDetail.setProperty("accountStatus", "disabled");
    log.warn("Account Disabled Exception (401 Unauthorized): {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
  }

  @ExceptionHandler(AccountExpiredException.class)
  protected ResponseEntity<Object> handleAccountExpired(
      AccountExpiredException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED,
            "The user account has expired. Please contact an administrator to renew your account.");
    problemDetail.setTitle("AccountExpiredException: Account Expired");
    problemDetail.setType(
        URI.create(
            "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/AccountExpiredException.html"));
    problemDetail.setProperty("accountStatus", "expired");
    log.warn("Account Expired Exception (401 Unauthorized): {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
  }

  @ExceptionHandler(CredentialsExpiredException.class)
  protected ResponseEntity<Object> handleCredentialsExpired(
      CredentialsExpiredException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED,
            "The user credentials (password) have expired. Please reset or update your password to proceed.");
    problemDetail.setTitle("CredentialsExpiredException: Credentials Expired");
    problemDetail.setType(
        URI.create(
            "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/CredentialsExpiredException.html"));
    problemDetail.setProperty("credentialsStatus", "expired");
    log.warn("Credentials Expired Exception (401 Unauthorized): {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
  }

  @ExceptionHandler(AccountStatusException.class)
  protected ResponseEntity<Object> handleAccountStatus(
      AccountStatusException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED,
            ex.getMessage() != null && !ex.getMessage().trim().isEmpty()
                ? ex.getMessage()
                : "Authentication failed due to the current status of the user account.");
    problemDetail.setTitle("AccountStatusException: Account Status Error");
    problemDetail.setType(
        URI.create(
            "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/AccountStatusException.html"));
    log.warn("Account Status Exception (401 Unauthorized): {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
  }

  @ExceptionHandler(InsufficientAuthenticationException.class)
  protected ResponseEntity<Object> handleInsufficientAuthentication(
      InsufficientAuthenticationException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED,
            "Full authentication is required to access this resource. Please provide a valid Bearer token.");
    problemDetail.setTitle("InsufficientAuthenticationException: Insufficient Authentication");
    problemDetail.setType(
        URI.create(
            "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/InsufficientAuthenticationException.html"));
    problemDetail.setProperty("auth", "insufficient");
    log.warn("Insufficient Authentication Exception (401 Unauthorized): {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
  }

  @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
  protected ResponseEntity<Object> handleAuthenticationCredentialsNotFound(
      AuthenticationCredentialsNotFoundException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED,
            "No authentication credentials were found in the security context to process this request.");
    problemDetail.setTitle("AuthenticationCredentialsNotFoundException: Credentials Not Found");
    problemDetail.setType(
        URI.create(
            "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/AuthenticationCredentialsNotFoundException.html"));
    problemDetail.setProperty("auth", "credentials_not_found");
    log.warn(
        "Authentication Credentials Not Found Exception (401 Unauthorized): {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
  }

  @ExceptionHandler(JwtException.class)
  protected ResponseEntity<Object> handleJwtException(JwtException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED,
            "The provided JSON Web Token is invalid, malformed, or has expired. Please authenticate again.");
    problemDetail.setTitle("JwtException: Invalid or Expired Token");
    problemDetail.setType(
        URI.create(
            "https://javadoc.io/doc/io.jsonwebtoken/jjwt-api/latest/io/jsonwebtoken/JwtException.html"));
    problemDetail.setProperty("token", "invalid_or_expired");
    log.warn("JWT Exception (401 Unauthorized): {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
  }

  @ExceptionHandler(AuthenticationException.class)
  protected ResponseEntity<Object> handleAuthentication(
      AuthenticationException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED,
            ex.getMessage() != null && !ex.getMessage().trim().isEmpty()
                ? ex.getMessage()
                : "An authentication error occurred while processing the request.");
    problemDetail.setTitle("AuthenticationException: Authentication Failed");
    problemDetail.setType(
        URI.create(
            "https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/core/AuthenticationException.html"));
    problemDetail.setProperty("error", "Authentication failed");
    log.warn("General Authentication Exception (401 Unauthorized): {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
  }

  @ExceptionHandler(TooManyLoginAttemptsException.class)
  protected ResponseEntity<Object> handleTooManyLoginAttempts(
      TooManyLoginAttemptsException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.TOO_MANY_REQUESTS,
            ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "Demasiados intentos fallidos. Tu cuenta ha sido bloqueada temporalmente.");
    problemDetail.setTitle("TooManyLoginAttemptsException: Too Many Requests");
    problemDetail.setProperty("error", "Account temporarily blocked");
    log.warn("Too Many Login Attempts (429 Too Many Requests): {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(problemDetail);
  }
}
