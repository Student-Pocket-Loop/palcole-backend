package org.vedruna.palcole.controller.handler;

import jakarta.persistence.RollbackException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import jakarta.validation.ValidationException;
import java.net.URI;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.vedruna.palcole.domain.exception.ResourceAlreadyExistsException;
import org.vedruna.palcole.domain.exception.ResourceNotFoundException;
import org.vedruna.palcole.domain.exception.roles.DefaultRoleNotFoundException;
import org.vedruna.palcole.domain.exception.users.DefaultUserNotFoundException;

/**
 * Controlador de Asesoramiento REST (Global Exception Handler) para gestionar las excepciones
 * lanzadas por la aplicación y convertirlas en respuestas HTTP estandarizadas (ProblemDetail).
 *
 * <p>Extiende {@link ResponseEntityExceptionHandler} para manejar las excepciones internas del
 * framework Spring y añade métodos {@code @ExceptionHandler} para capturar excepciones específicas
 * del dominio, validación y persistencia.
 */
@AllArgsConstructor
@Slf4j
@RestControllerAdvice
public class HandlerExceptionController extends ResponseEntityExceptionHandler {

  /**
   * Maneja cualquier excepción de dominio que extienda {@code ResourceNotFoundException}
   * (UserNotFoundException, RoleNotFoundException, CategoryNotFoundException, ...). Genera una
   * respuesta 404 Not Found.
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  protected ResponseEntity<Object> handleResourceNotFound(
      ResourceNotFoundException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "El recurso solicitado no fue encontrado.");
    problemDetail.setTitle(ex.getClass().getSimpleName() + ": Resource Not Found");
    log.warn("{} (404 Not Found): {}", ex.getClass().getSimpleName(), ex.getMessage());
    return handleExceptionInternal(
        ex, problemDetail, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  /**
   * Maneja cualquier excepción de dominio que extienda {@code ResourceAlreadyExistsException}
   * (UserAlreadyExistsException, ...). Genera una respuesta 409 Conflict.
   */
  @ExceptionHandler(ResourceAlreadyExistsException.class)
  protected ResponseEntity<Object> handleResourceAlreadyExists(
      ResourceAlreadyExistsException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT,
            ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "El recurso ya existe.");
    problemDetail.setTitle(ex.getClass().getSimpleName() + ": Resource Already Exists");
    log.warn("{} (409 Conflict): {}", ex.getClass().getSimpleName(), ex.getMessage());
    return handleExceptionInternal(
        ex, problemDetail, new HttpHeaders(), HttpStatus.CONFLICT, request);
  }

  /**
   * Maneja las excepciones de datos iniciales faltantes ({@code DefaultRoleNotFoundException} y
   * {@code DefaultUserNotFoundException}). Al no ser un error del cliente sino de configuración de
   * la base de datos, genera una respuesta 500 Internal Server Error.
   */
  @ExceptionHandler({DefaultRoleNotFoundException.class, DefaultUserNotFoundException.class})
  protected ResponseEntity<Object> handleDefaultDataNotFound(
      NoSuchElementException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "Error interno: faltan datos iniciales obligatorios en la base de datos.");
    problemDetail.setTitle(ex.getClass().getSimpleName() + ": Missing Default Data");
    log.error("{} (500 Internal Server Error): {}", ex.getClass().getSimpleName(), ex.getMessage());
    return handleExceptionInternal(
        ex, problemDetail, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

  /**
   * Maneja excepciones cuando un argumento de método ({@code @RequestParam} o
   * {@code @PathVariable}) no se puede convertir al tipo de dato requerido. Genera una respuesta
   * 400 Bad Request.
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            String.format(
                "The parameter '%s' with value '%s' must be of type '%s'.",
                ex.getName(),
                ex.getValue(),
                ex.getRequiredType() != null
                    ? ex.getRequiredType().getSimpleName()
                    : "a different type"));
    problemDetail.setTitle("MethodArgumentTypeMismatchException: Invalid Argument Type");
    problemDetail.setType(
        URI.create(
            "https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/method/annotation/MethodArgumentTypeMismatchException.html"));
    log.warn("Mismatched Argument Type (400): {}", ex.getMessage());
    return handleExceptionInternal(
        ex, problemDetail, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  /**
   * Maneja excepciones cuando se aplica una anotación de validación a un tipo de dato incompatible.
   * Genera una respuesta 400 Bad Request.
   */
  @ExceptionHandler(UnexpectedTypeException.class)
  protected ResponseEntity<Object> handleUnexpectedType(
      UnexpectedTypeException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            "A type mismatch was encountered during validation processing. Check if validation annotations are applied correctly to the target field's type.");
    problemDetail.setTitle("UnexpectedTypeException: Unexpected Validation Type");
    problemDetail.setType(
        URI.create(
            "https://jakarta.ee/specifications/bean-validation/3.0/apidocs/jakarta/validation/UnexpectedTypeException.html"));
    log.warn("Unexpected Type Exception during validation (400): {}", ex.getMessage());
    return handleExceptionInternal(
        ex, problemDetail, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  /**
   * Maneja excepciones lanzadas por la aplicación debido a argumentos que violan reglas de negocio
   * o valores inválidos. Genera una respuesta 400 Bad Request.
   */
  @ExceptionHandler(IllegalArgumentException.class)
  protected ResponseEntity<Object> handleIllegalArgument(
      IllegalArgumentException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "The request argument is invalid due to a violation of business rules.");
    problemDetail.setTitle("IllegalArgumentException: Invalid Business Argument");
    problemDetail.setType(
        URI.create(
            "https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/IllegalArgumentException.html"));
    log.warn("Illegal Argument Exception (400): {}", ex.getMessage());
    return handleExceptionInternal(
        ex, problemDetail, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  /**
   * Maneja la excepción de Spring Data {@code EmptyResultDataAccessException}. Genera una respuesta
   * 404 Not Found.
   */
  @ExceptionHandler(EmptyResultDataAccessException.class)
  protected ResponseEntity<Object> handleEmptyResultDataAccess(
      EmptyResultDataAccessException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            "The requested resource was not found in the database. It may have been deleted or the identifier is incorrect.");
    problemDetail.setTitle("EmptyResultDataAccessException: Resource Not Found");
    problemDetail.setType(
        URI.create(
            "https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/dao/EmptyResultDataAccessException.html"));
    log.warn("Empty Result Data Access Exception (404): {}", ex.getMessage());
    return handleExceptionInternal(
        ex, problemDetail, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  /**
   * Maneja la excepción de Spring Data {@code DataIntegrityViolationException} (clave única
   * duplicada, violación de clave foránea). Genera una respuesta 409 Conflict.
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  protected ResponseEntity<Object> handleDataIntegrityViolation(
      DataIntegrityViolationException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT,
            "The request could not be completed due to a conflict with existing data or a data constraint. This may be a duplicate unique entry or a foreign key violation.");
    problemDetail.setTitle("DataIntegrityViolationException: Data Conflict");
    problemDetail.setType(
        URI.create(
            "https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/dao/DataIntegrityViolationException.html"));
    log.warn("Data Integrity Violation (409 Conflict): {}", ex.getMessage());
    return handleExceptionInternal(
        ex, problemDetail, new HttpHeaders(), HttpStatus.CONFLICT, request);
  }

  /**
   * Maneja errores cuando el cuerpo JSON de la petición no se puede deserializar o está mal
   * formado. Genera una respuesta 400 Bad Request.
   */
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            ex.getRootCause() != null
                ? ex.getRootCause().getLocalizedMessage()
                : "The request body could not be read or is malformed. Please check the JSON syntax and data types.");
    problemDetail.setTitle("HttpMessageNotReadableException: Malformed Body");
    problemDetail.setType(
        URI.create(
            "https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/converter/HttpMessageNotReadableException.html"));
    log.warn("HTTP Message Not Readable (Malformed Body): {}", ex.getMessage());
    return handleExceptionInternal(
        ex, problemDetail, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  /**
   * Maneja fallos de validación JSR 380 en parámetros {@code @RequestParam} o
   * {@code @PathVariable}. Genera una respuesta 400 Bad Request.
   */
  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<Object> handleConstraintViolation(
      ConstraintViolationException ex, WebRequest request) {
    Map<String, String> errors =
        ex.getConstraintViolations().stream()
            .collect(
                Collectors.toMap(
                    violation -> violation.getPropertyPath().toString(),
                    ConstraintViolation::getMessage,
                    (existing, replacement) -> existing));
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            "One or more constraints were violated. Please check the 'errors' property for details on the invalid fields.");
    problemDetail.setTitle("ConstraintViolationException: Constraint Violation");
    problemDetail.setProperty("errors", errors);
    problemDetail.setType(
        URI.create(
            "https://jakarta.ee/specifications/bean-validation/3.0/apidocs/jakarta/validation/ConstraintViolationException.html"));
    log.warn("Constraint Violation Exception (400 Bad Request): {}", ex.getMessage());
    return handleExceptionInternal(
        ex, problemDetail, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  /**
   * Maneja fallos de validación JSR 380 en DTOs anotados con {@code @Valid}. Genera una respuesta
   * 400 Bad Request.
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    Map<String, String> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .collect(
                Collectors.toMap(
                    FieldError::getField,
                    error ->
                        (error.getDefaultMessage() != null)
                            ? error.getDefaultMessage()
                            : "Invalid value",
                    (existing, replacement) -> existing));
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            "The request body contains invalid data. Please correct the fields listed in the 'errors' property.");
    problemDetail.setTitle("MethodArgumentNotValidException: Validation Error");
    problemDetail.setProperty("errors", errors);
    problemDetail.setType(
        URI.create(
            "https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/MethodArgumentNotValidException.html"));
    log.warn("Method Argument Not Valid Exception (400 Bad Request): {}", ex.getMessage());
    return handleExceptionInternal(ex, problemDetail, headers, HttpStatus.BAD_REQUEST, request);
  }

  /**
   * Maneja la excepción general de validación {@code ValidationException}. Genera una respuesta 400
   * Bad Request.
   */
  @ExceptionHandler(ValidationException.class)
  protected ResponseEntity<Object> handleValidation(ValidationException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "The request data is invalid. One or more validation rules were violated.");
    problemDetail.setTitle("ValidationException: Validation Failed");
    problemDetail.setType(
        URI.create(
            "https://jakarta.ee/specifications/bean-validation/3.0/apidocs/jakarta/validation/ValidationException.html"));
    log.warn("Validation Exception (400 Bad Request): {}", ex.getMessage());
    return handleExceptionInternal(
        ex, problemDetail, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  /**
   * Maneja excepciones de {@code RollbackException} en transacciones de base de datos. Genera una
   * respuesta 500 Internal Server Error.
   */
  @ExceptionHandler(RollbackException.class)
  protected ResponseEntity<Object> handleRollBack(RollbackException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred while processing the transaction. The operation could not be completed.");
    problemDetail.setTitle("RollbackException: Internal Server Error");
    problemDetail.setType(
        URI.create(
            "https://jakarta.ee/specifications/persistence/3.0/apidocs/jakarta.persistence/jakarta/persistence/rollbackexception"));
    log.error("Transaction Rollback Failure (500 Internal Error): ", ex);
    return handleExceptionInternal(
        ex, problemDetail, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

  /**
   * Maneja peticiones con métodos HTTP no soportados para un endpoint. Genera una respuesta 405
   * Method Not Allowed.
   */
  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.METHOD_NOT_ALLOWED,
            ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "The HTTP method used for this resource is not supported. Please check the allowed methods.");
    problemDetail.setTitle("HttpRequestMethodNotSupportedException: HTTP Method Not Allowed");
    problemDetail.setType(
        URI.create(
            "https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/HttpRequestMethodNotSupportedException.html"));
    if (ex.getSupportedHttpMethods() != null && !ex.getSupportedHttpMethods().isEmpty()) {
      headers.setAllow(ex.getSupportedHttpMethods());
      Set<String> allowedMethodsStrings =
          ex.getSupportedHttpMethods().stream().map(HttpMethod::name).collect(Collectors.toSet());
      problemDetail.setProperty("allowedMethods", allowedMethodsStrings);
    }
    log.warn("HTTP Method Not Supported (405): {}", ex.getMessage());
    return handleExceptionInternal(
        ex, problemDetail, headers, HttpStatus.METHOD_NOT_ALLOWED, request);
  }

  /**
   * Maneja propiedades inválidas en parámetros de ordenación o paginación ({@code Pageable}).
   * Genera una respuesta 400 Bad Request.
   */
  @ExceptionHandler(PropertyReferenceException.class)
  protected ResponseEntity<Object> handlePropertyReference(
      PropertyReferenceException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            ex.getMessage() != null && !ex.getMessage().isBlank()
                ? ex.getMessage()
                : "The requested property for sorting or searching does not exist on the resource.");
    problemDetail.setTitle("PropertyReferenceException: Invalid Property Reference");
    problemDetail.setProperty("invalidProperty", ex.getPropertyName());
    problemDetail.setType(
        URI.create(
            "https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/mapping/PropertyReferenceException.html"));
    log.warn("Property Reference Exception (400 Bad Request): {}", ex.getMessage());
    return handleExceptionInternal(
        ex, problemDetail, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  /**
   * Manejador genérico (fallback) para cualquier excepción no contemplada explícitamente. Genera
   * una respuesta 500 Internal Server Error.
   */
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected internal server error occurred. Please try again later.");
    problemDetail.setTitle("Internal Server Error");
    log.error("Unhandled Internal Server Error (500): ", ex);
    return handleExceptionInternal(
        ex, problemDetail, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }
}
