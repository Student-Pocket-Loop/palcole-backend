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
import java.util.NoSuchElementException;
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
import org.springframework.data.core.TypeInformation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
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
    ResourceNotFoundException exInput = new ResourceNotFoundException("Curso no encontrado");

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
          assertEquals("ResourceNotFoundException: Resource Not Found", body.getTitle());
          assertEquals("Curso no encontrado", body.getDetail());
        });
  }

  @Test
  @DisplayName("Test handler handleResourceNotFound con mensaje en blanco")
  void handleResourceNotFoundBlankMessageTest() {

    // INPUT
    ResourceNotFoundException exInput = new ResourceNotFoundException("   ");

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerExceptionController.handleResourceNotFound(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals("El recurso solicitado no fue encontrado.", body.getDetail());
        });
  }

  @Test
  @DisplayName("Test handler handleResourceAlreadyExists Ok")
  void handleResourceAlreadyExistsOkTest() {

    // INPUT
    ResourceAlreadyExistsException exInput =
        new ResourceAlreadyExistsException("El recurso ya existe");

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
          assertEquals("ResourceAlreadyExistsException: Resource Already Exists", body.getTitle());
          assertEquals("El recurso ya existe", body.getDetail());
        });
  }

  @Test
  @DisplayName("Test handler handleResourceAlreadyExists con mensaje en blanco")
  void handleResourceAlreadyExistsBlankMessageTest() {

    // INPUT
    ResourceAlreadyExistsException exInput = new ResourceAlreadyExistsException("  ");

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerExceptionController.handleResourceAlreadyExists(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals("El recurso ya existe.", body.getDetail());
        });
  }

  @Test
  @DisplayName("Test handler handleDefaultDataNotFound con DefaultRoleNotFoundException Ok")
  void handleDefaultRoleNotFoundOkTest() {

    // INPUT
    NoSuchElementException exInput = new DefaultRoleNotFoundException("USER");

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
  @DisplayName(
      "Test handler handleDefaultDataNotFound con DefaultUserNotFoundException y mensaje en blanco")
  void handleDefaultUserNotFoundBlankMessageTest() {

    // INPUT
    NoSuchElementException exInput = mock(DefaultUserNotFoundException.class);
    when(exInput.getMessage()).thenReturn("   ");

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
          assertEquals(
              "Error interno: faltan datos iniciales obligatorios en la base de datos.",
              body.getDetail());
        });
  }

  @Test
  @DisplayName("Test handler handleMethodArgumentTypeMismatch Ok")
  void handleMethodArgumentTypeMismatchOkTest() {

    // INPUT
    MethodArgumentTypeMismatchException exInput =
        new MethodArgumentTypeMismatchException(
            "abc", Long.class, "id", null, new NumberFormatException("For input string: \"abc\""));

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
              "The parameter 'id' with value 'abc' must be of type 'Long'.", body.getDetail());
          assertEquals(
              URI.create(
                  "https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/method/annotation/MethodArgumentTypeMismatchException.html"),
              body.getType());
        });
  }

  @Test
  @DisplayName("Test handler handleMethodArgumentTypeMismatch con requiredType nulo")
  void handleMethodArgumentTypeMismatchNullTypeTest() {

    // INPUT
    MethodArgumentTypeMismatchException exInput =
        new MethodArgumentTypeMismatchException("abc", null, "id", null, new RuntimeException());

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerExceptionController.handleMethodArgumentTypeMismatch(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(
              "The parameter 'id' with value 'abc' must be of type 'a different type'.",
              body.getDetail());
        });
  }

  @Test
  @DisplayName("Test handler handleUnexpectedType Ok")
  void handleUnexpectedTypeOkTest() {

    // INPUT
    UnexpectedTypeException exInput = new UnexpectedTypeException("Unexpected validation type");

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
    IllegalArgumentException exInput = new IllegalArgumentException("Edad no válida");

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
          assertEquals("Edad no válida", body.getDetail());
          assertEquals(
              URI.create(
                  "https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/IllegalArgumentException.html"),
              body.getType());
        });
  }

  @Test
  @DisplayName("Test handler handleIllegalArgument con mensaje en blanco")
  void handleIllegalArgumentBlankMessageTest() {

    // INPUT
    IllegalArgumentException exInput = new IllegalArgumentException("   ");

    // EJECUCIÓN
    ResponseEntity<Object> res = handlerExceptionController.handleIllegalArgument(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(
              "The request argument is invalid due to a violation of business rules.",
              body.getDetail());
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
  @DisplayName("Test handler handleHttpMessageNotReadable con rootCause")
  void handleHttpMessageNotReadableWithRootCauseTest() {

    // INPUT
    Throwable rootCause = new RuntimeException("JSON parse error");
    HttpMessageNotReadableException exInput =
        new HttpMessageNotReadableException(
            "Malformed JSON", rootCause, mock(HttpInputMessage.class));

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerExceptionController.handleHttpMessageNotReadable(
            exInput, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

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
  @DisplayName("Test handler handleHttpMessageNotReadable sin rootCause")
  void handleHttpMessageNotReadableWithoutRootCauseTest() {

    // INPUT
    HttpMessageNotReadableException exInput =
        new HttpMessageNotReadableException("Malformed JSON", mock(HttpInputMessage.class));

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerExceptionController.handleHttpMessageNotReadable(
            exInput, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(
              "The request body could not be read or is malformed. Please check the JSON syntax and data types.",
              body.getDetail());
        });
  }

  @Test
  @DisplayName("Test handler handleConstraintViolation Ok")
  void handleConstraintViolationOkTest() {

    // INPUT
    @SuppressWarnings("unchecked")
    ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
    Path path = mock(Path.class);
    when(path.toString()).thenReturn("email");
    when(violation.getPropertyPath()).thenReturn(path);
    when(violation.getMessage()).thenReturn("must be a well-formed email address");

    ConstraintViolationException exInput = new ConstraintViolationException(Set.of(violation));

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
              "One or more constraints were violated. Please check the 'errors' property for details on the invalid fields.",
              body.getDetail());
          assertEquals(
              URI.create(
                  "https://jakarta.ee/specifications/bean-validation/3.0/apidocs/jakarta/validation/ConstraintViolationException.html"),
              body.getType());
          assertNotNull(body.getProperties());
          assertEquals(
              Map.of("email", "must be a well-formed email address"),
              body.getProperties().get("errors"));
        });
  }

  @Test
  @DisplayName("Test handler handleMethodArgumentNotValid Ok")
  void handleMethodArgumentNotValidOkTest() {

    // INPUT
    BindingResult bindingResult = mock(BindingResult.class);
    FieldError fieldError = new FieldError("userDto", "username", "no puede estar vacío");
    when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

    MethodArgumentNotValidException exInput = mock(MethodArgumentNotValidException.class);
    when(exInput.getBindingResult()).thenReturn(bindingResult);

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerExceptionController.handleMethodArgumentNotValid(
            exInput, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.BAD_REQUEST.value(), body.getStatus());
          assertEquals("MethodArgumentNotValidException: Validation Error", body.getTitle());
          assertEquals(
              "The request body contains invalid data. Please correct the fields listed in the 'errors' property.",
              body.getDetail());
          assertEquals(
              URI.create(
                  "https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/MethodArgumentNotValidException.html"),
              body.getType());
          assertNotNull(body.getProperties());
          assertEquals(
              Map.of("username", "no puede estar vacío"), body.getProperties().get("errors"));
        });
  }

  @Test
  @DisplayName("Test handler handleValidation Ok")
  void handleValidationOkTest() {

    // INPUT
    ValidationException exInput = new ValidationException("Error de validación personalizado");

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
          assertEquals("Error de validación personalizado", body.getDetail());
          assertEquals(
              URI.create(
                  "https://jakarta.ee/specifications/bean-validation/3.0/apidocs/jakarta/validation/ValidationException.html"),
              body.getType());
        });
  }

  @Test
  @DisplayName("Test handler handleValidation con mensaje en blanco")
  void handleValidationBlankMessageTest() {

    // INPUT
    ValidationException exInput = new ValidationException("   ");

    // EJECUCIÓN
    ResponseEntity<Object> res = handlerExceptionController.handleValidation(exInput, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(
              "The request data is invalid. One or more validation rules were violated.",
              body.getDetail());
        });
  }

  @Test
  @DisplayName("Test handler handleRollBack Ok")
  void handleRollBackOkTest() {

    // INPUT
    RollbackException exInput = new RollbackException("Transaction rolled back");

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
    HttpHeaders headers = new HttpHeaders();

    // EJECUCIÓN
    ResponseEntity<Object> res =
        handlerExceptionController.handleHttpRequestMethodNotSupported(
            exInput, headers, HttpStatus.METHOD_NOT_ALLOWED, request);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.METHOD_NOT_ALLOWED, res.getStatusCode());
          assertEquals(Set.of(HttpMethod.GET, HttpMethod.POST), headers.getAllow());
          ProblemDetail body = assertInstanceOf(ProblemDetail.class, res.getBody());
          assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), body.getStatus());
          assertEquals(
              "HttpRequestMethodNotSupportedException: HTTP Method Not Allowed", body.getTitle());
          assertEquals("Request method 'DELETE' is not supported", body.getDetail());
          assertEquals(
              URI.create(
                  "https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/HttpRequestMethodNotSupportedException.html"),
              body.getType());
          assertNotNull(body.getProperties());
          assertEquals(Set.of("GET", "POST"), body.getProperties().get("allowedMethods"));
        });
  }

  @Test
  @DisplayName("Test handler handlePropertyReference Ok")
  void handlePropertyReferenceOkTest() {

    // INPUT
    PropertyReferenceException exInput =
        new PropertyReferenceException("invalidField", TypeInformation.of(Object.class), List.of());

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
          assertEquals(exInput.getMessage(), body.getDetail());
          assertEquals(
              URI.create(
                  "https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/mapping/PropertyReferenceException.html"),
              body.getType());
          assertNotNull(body.getProperties());
          assertEquals("invalidField", body.getProperties().get("invalidProperty"));
        });
  }

  @Test
  @DisplayName("Test handler handleGenericException Ok")
  void handleGenericExceptionOkTest() {

    // INPUT
    Exception exInput = new Exception("Unexpected failure");

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
