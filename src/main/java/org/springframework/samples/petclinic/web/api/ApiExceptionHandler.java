package org.springframework.samples.petclinic.web.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.ErrorDto;
import org.springframework.samples.petclinic.model.FieldErrorDto;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(SuperFatalErrorException.class)
  public ResponseEntity<?> badRequest(HttpServletRequest req, Exception exception) {
    Map<String, Object> result = new HashMap<>();
    result.put("status", HttpStatus.BAD_REQUEST);
    result.put("message", exception.getMessage());
    return ResponseEntity.badRequest().body(result);
  }

  /**
   * Credits to: Willie Wheeler
   * (http://springinpractice.com/2013/10/09/generating-json-error-object-responses-with-spring-web-mvc)
   *
   * @author Willie Wheeler (@williewheeler)
   */
  @ExceptionHandler(InvalidRequestException.class)
  protected ResponseEntity<Object> handleInvalidRequest(
      InvalidRequestException ire, WebRequest request) {
    log.info("InvalidRequestException caught", ire);
    List<FieldErrorDto> fieldErrorDtos = new ArrayList<>();

    List<FieldError> fieldErrors = ire.getErrors().getFieldErrors();
    for (FieldError fieldError : fieldErrors) {
      FieldErrorDto fieldErrorDto = new FieldErrorDto();
      fieldErrorDto.setResource(fieldError.getObjectName());
      fieldErrorDto.setField(fieldError.getField());
      fieldErrorDto.setCode(fieldError.getCode());
      fieldErrorDto.setMessage(fieldError.getDefaultMessage());
      fieldErrorDtos.add(fieldErrorDto);
    }

    ErrorDto error = new ErrorDto("InvalidRequest", ire.getMessage());
    error.setFieldErrors(fieldErrorDtos);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return handleExceptionInternal(ire, error, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
  }

  @ExceptionHandler(BadRequestException.class)
  protected ResponseEntity<Object> handleBadRequest(BadRequestException bre, WebRequest request) {
    log.info("BadRequestException caught", bre);

    ErrorDto error = new ErrorDto("BadRequest", bre.getMessage());
    error.addGlobalError(bre.getMessage());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return handleExceptionInternal(bre, error, headers, HttpStatus.BAD_REQUEST, request);
  }
}
