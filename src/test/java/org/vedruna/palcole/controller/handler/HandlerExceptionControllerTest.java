package org.vedruna.palcole.controller.handler;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.persistence.RollbackException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.UnexpectedTypeException;
import jakarta.validation.ValidationException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.vedruna.palcole.domain.exception.ResourceAlreadyExistsException;
import org.vedruna.palcole.domain.exception.ResourceNotFoundException;
import org.vedruna.palcole.domain.exception.roles.DefaultRoleNotFoundException;
import org.vedruna.palcole.domain.exception.users.DefaultUserNotFoundException;

@ExtendWith(MockitoExtension.class)
class HandlerExceptionControllerTest {

  @Mock private WebRequest request;

  @InjectMocks private HandlerExceptionController handlerExceptionController;

  @Test
  @DisplayName("Test handler handleResourceNotFound Ok")
  void handleResourceNotFoundOkTest() {

    // INPUT
    ResourceNotFoundException exInput = mock(ResourceNotFoundException.class);

    // MOCKS
    when(exInput.getMessage()).thenReturn("El recurso solicitado no fue encontrado");

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerExceptionController.handleResourceNotFound(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.NOT_FOUND.value(), body.getStatus());
          assertEquals(
              exInput.getClass().getSimpleName() + ": Resource Not Found", body.getTitle());
          assertEquals("El recurso solicitado no fue encontrado", body.getDetail());
        });
  }

  @Test
  @DisplayName("Test handler handleResourceAlreadyExists Ok")
  void handleResourceAlreadyExistsOkTest() {

    // INPUT
    ResourceAlreadyExistsException exInput = mock(ResourceAlreadyExistsException.class);

    // MOCKS
    when(exInput.getMessage()).thenReturn("El recurso ya existe");

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerExceptionController.handleResourceAlreadyExists(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.CONFLICT.value(), body.getStatus());
          assertEquals(
              exInput.getClass().getSimpleName() + ": Resource Already Exists", body.getTitle());
          assertEquals("El recurso ya existe", body.getDetail());
        });
  }

  @Test
  @DisplayName("Test handler handleDefaultDataNotFound DefaultRoleNotFoundException Ok")
  void handleDefaultDataNotFoundRoleOkTest() {

    // INPUT
    DefaultRoleNotFoundException exInput = new DefaultRoleNotFoundException("USER");

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerExceptionController.handleDefaultDataNotFound(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), body.getStatus());
          assertEquals("DefaultRoleNotFoundException: Missing Default Data", body.getTitle());
          assertEquals(
              "No existe el role por defecto 'USER' en la base de datos", body.getDetail());
        });
  }

  @Test
  @DisplayName("Test handler handleDefaultDataNotFound DefaultUserNotFoundException Ok")
  void handleDefaultDataNotFoundUserOkTest() {

    // INPUT
    DefaultUserNotFoundException exInput = mock(DefaultUserNotFoundException.class);

    // MOCKS
    when(exInput.getMessage()).thenReturn("No existe el usuario por defecto en la base de datos");

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerExceptionController.handleDefaultDataNotFound(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), body.getStatus());
          assertEquals("DefaultUserNotFoundException: Missing Default Data", body.getTitle());
          assertEquals("No existe el usuario por defecto en la base de datos", body.getDetail());
        });
  }

  @Test
  @DisplayName("Test handler handleMethodArgumentTypeMismatch Ok")
  void handleMethodArgumentTypeMismatchOkTest() {

    // INPUT
    MethodArgumentTypeMismatchException exInput = mock(MethodArgumentTypeMismatchException.class);

    // MOCKS
    when(exInput.getName()).thenReturn("roleId");
    when(exInput.getValue()).thenReturn("abc");
    when(exInput.getRequiredType()).thenReturn((Class) Integer.class);

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerExceptionController.handleMethodArgumentTypeMismatch(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.BAD_REQUEST.value(), body.getStatus());
          assertEquals(
              "MethodArgumentTypeMismatchException: Invalid Argument Type", body.getTitle());
          assertEquals(
              "The parameter 'roleId' with value 'abc' must be of type 'Integer'.",
              body.getDetail());
          assertEquals(
              URI.create(
                  "https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/method/annotation/MethodArgumentTypeMismatchException.html"),
              body.getType());
        });
  }

  @Test
  @DisplayName("Test handler handleUnexpectedType Ok")
  void handleUnexpectedTypeOkTest() {

    // INPUT
    UnexpectedTypeException exInput = new UnexpectedTypeException("Tipo inesperado");

    // EJECUCIÓN
    ResponseEntity<Object> res = handlerExceptionController.handleUnexpectedType(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.BAD_REQUEST.value(), body.getStatus());
          assertEquals("UnexpectedTypeException: Unexpected Validation Type", body.getTitle());
          assertEquals(
              "A type mismatch was encountered during validation processing. Check if validation annotations are applied correctly to the target field's type.",
              body.getDetail());
          assertEquals(
              URI.create(
                  "https://jakarta.ee/specifications/bean-validation/3.0/apidocs/jakarta/validation/UnexpectedTypeException.html"),
              body.getType());
        });
  }

  @Test
  @DisplayName("Test handler handleIllegalArgument Ok")
  void handleIllegalArgumentOkTest() {

    // INPUT
    IllegalArgumentException exInput =
        new IllegalArgumentException("Propiedad de ordenación inválida");

    // EJECUCIÓN
    ResponseEntity<Object> res = handlerExceptionController.handleIllegalArgument(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.BAD_REQUEST.value(), body.getStatus());
          assertEquals("IllegalArgumentException: Invalid Business Argument", body.getTitle());
          assertEquals("Propiedad de ordenación inválida", body.getDetail());
          assertEquals(
              URI.create(
                  "https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/IllegalArgumentException.html"),
              body.getType());
        });
  }

  @Test
  @DisplayName("Test handler handleEmptyResultDataAccess Ok")
  void handleEmptyResultDataAccessOkTest() {

    // INPUT
    EmptyResultDataAccessException exInput = new EmptyResultDataAccessException(1);

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerExceptionController.handleEmptyResultDataAccess(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.NOT_FOUND.value(), body.getStatus());
          assertEquals("EmptyResultDataAccessException: Resource Not Found", body.getTitle());
          assertEquals(
              "The requested resource was not found in the database. It may have been deleted or the identifier is incorrect.",
              body.getDetail());
          assertEquals(
              URI.create(
                  "https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/dao/EmptyResultDataAccessException.html"),
              body.getType());
        });
  }

  @Test
  @DisplayName("Test handler handleDataIntegrityViolation Ok")
  void handleDataIntegrityViolationOkTest() {

    // INPUT
    DataIntegrityViolationException exInput =
        new DataIntegrityViolationException("Duplicate entry");

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerExceptionController.handleDataIntegrityViolation(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.CONFLICT.value(), body.getStatus());
          assertEquals("DataIntegrityViolationException: Data Conflict", body.getTitle());
          assertEquals(
              "The request could not be completed due to a conflict with existing data or a data constraint. This may be a duplicate unique entry or a foreign key violation.",
              body.getDetail());
          assertEquals(
              URI.create(
                  "https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/dao/DataIntegrityViolationException.html"),
              body.getType());
        });
  }

  @Test
  @DisplayName("Test handler handleHttpMessageNotReadable Ok")
  void handleHttpMessageNotReadableOkTest() {

    // INPUT
    HttpMessageNotReadableException exInput = mock(HttpMessageNotReadableException.class);
    HttpHeaders headersInput = new HttpHeaders();

    // MOCKS
    when(exInput.getRootCause()).thenReturn(new RuntimeException("JSON parse error"));

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerExceptionController.handleHttpMessageNotReadable(
            exInput, headersInput, HttpStatus.BAD_REQUEST, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.BAD_REQUEST.value(), body.getStatus());
          assertEquals("HttpMessageNotReadableException: Malformed Body", body.getTitle());
          assertEquals("JSON parse error", body.getDetail());
          assertEquals(
              URI.create(
                  "https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/converter/HttpMessageNotReadableException.html"),
              body.getType());
        });
  }

  @Test
  @DisplayName("Test handler handleConstraintViolation Ok")
  void handleConstraintViolationOkTest() {

    // INPUT
    ConstraintViolation<?> violationMock = mock(ConstraintViolation.class);
    Path pathMock = mock(Path.class);
    ConstraintViolationException exInput = new ConstraintViolationException(Set.of(violationMock));

    // MOCKS
    when(violationMock.getPropertyPath()).thenReturn(pathMock);
    when(pathMock.toString()).thenReturn("roleId");
    when(violationMock.getMessage()).thenReturn("must be greater than 0");

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerExceptionController.handleConstraintViolation(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.BAD_REQUEST.value(), body.getStatus());
          assertEquals("ConstraintViolationException: Constraint Violation", body.getTitle());
          assertEquals(
              Map.of("roleId", "must be greater than 0"), body.getProperties().get("errors"));
          assertEquals(
              URI.create(
                  "https://jakarta.ee/specifications/bean-validation/3.0/apidocs/jakarta/validation/ConstraintViolationException.html"),
              body.getType());
        });
  }

  @Test
  @DisplayName("Test handler handleMethodArgumentNotValid Ok")
  void handleMethodArgumentNotValidOkTest() {

    // INPUT
    MethodArgumentNotValidException exInput = mock(MethodArgumentNotValidException.class);
    BindingResult bindingResultMock = mock(BindingResult.class);
    HttpHeaders headersInput = new HttpHeaders();
    List<FieldError> fieldErrors =
        List.of(new FieldError("insertRoleDTO", "name", "El nombre no puede estar vacío"));

    // MOCKS
    when(exInput.getBindingResult()).thenReturn(bindingResultMock);
    when(bindingResultMock.getFieldErrors()).thenReturn(fieldErrors);

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerExceptionController.handleMethodArgumentNotValid(
            exInput, headersInput, HttpStatus.BAD_REQUEST, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.BAD_REQUEST.value(), body.getStatus());
          assertEquals("MethodArgumentNotValidException: Validation Error", body.getTitle());
          assertEquals(
              Map.of("name", "El nombre no puede estar vacío"), body.getProperties().get("errors"));
          assertEquals(
              URI.create(
                  "https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/MethodArgumentNotValidException.html"),
              body.getType());
        });
  }

  @Test
  @DisplayName("Test handler handleValidation Ok")
  void handleValidationOkTest() {

    // INPUT
    ValidationException exInput = new ValidationException("Error de validación general");

    // EJECUCIÓN
    ResponseEntity<Object> res = handlerExceptionController.handleValidation(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.BAD_REQUEST.value(), body.getStatus());
          assertEquals("ValidationException: Validation Failed", body.getTitle());
          assertEquals("Error de validación general", body.getDetail());
          assertEquals(
              URI.create(
                  "https://jakarta.ee/specifications/bean-validation/3.0/apidocs/jakarta/validation/ValidationException.html"),
              body.getType());
        });
  }

  @Test
  @DisplayName("Test handler handleRollBack Ok")
  void handleRollBackOkTest() {

    // INPUT
    RollbackException exInput = new RollbackException("Error en transacción");

    // EJECUCIÓN
    ResponseEntity<Object> res = handlerExceptionController.handleRollBack(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), body.getStatus());
          assertEquals("RollbackException: Internal Server Error", body.getTitle());
          assertEquals(
              "An unexpected error occurred while processing the transaction. The operation could not be completed.",
              body.getDetail());
          assertEquals(
              URI.create(
                  "https://jakarta.ee/specifications/persistence/3.0/apidocs/jakarta.persistence/jakarta/persistence/rollbackexception"),
              body.getType());
        });
  }

  @Test
  @DisplayName("Test handler handleHttpRequestMethodNotSupported Ok")
  void handleHttpRequestMethodNotSupportedOkTest() {

    // INPUT
    HttpRequestMethodNotSupportedException exInput =
        new HttpRequestMethodNotSupportedException("DELETE", List.of("GET", "POST"));
    HttpHeaders headersInput = new HttpHeaders();

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerExceptionController.handleHttpRequestMethodNotSupported(
            exInput, headersInput, HttpStatus.METHOD_NOT_ALLOWED, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.METHOD_NOT_ALLOWED, res.getStatusCode());
          assertEquals(Set.of(HttpMethod.GET, HttpMethod.POST), res.getHeaders().getAllow());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), body.getStatus());
          assertEquals(
              "HttpRequestMethodNotSupportedException: HTTP Method Not Allowed", body.getTitle());
          assertEquals(Set.of("GET", "POST"), body.getProperties().get("allowedMethods"));
        });
  }

  @Test
  @DisplayName("Test handler handlePropertyReference Ok")
  void handlePropertyReferenceOkTest() {

    // INPUT
    PropertyReferenceException exInput = mock(PropertyReferenceException.class);

    // MOCKS
    when(exInput.getMessage()).thenReturn("No property 'invalidField' found for type 'Role'");
    when(exInput.getPropertyName()).thenReturn("invalidField");

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerExceptionController.handlePropertyReference(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.BAD_REQUEST.value(), body.getStatus());
          assertEquals("PropertyReferenceException: Invalid Property Reference", body.getTitle());
          assertEquals("No property 'invalidField' found for type 'Role'", body.getDetail());
          assertEquals("invalidField", body.getProperties().get("invalidProperty"));
          assertEquals(
              URI.create(
                  "https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/mapping/PropertyReferenceException.html"),
              body.getType());
        });
  }

  @Test
  @DisplayName("Test handler handleGenericException Ok")
  void handleGenericExceptionOkTest() {

    // INPUT
    Exception exInput = new RuntimeException("Error inesperado");

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerExceptionController.handleGenericException(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), body.getStatus());
          assertEquals("Internal Server Error", body.getTitle());
          assertEquals(
              "An unexpected internal server error occurred. Please try again later.",
              body.getDetail());
        });
  }
}
