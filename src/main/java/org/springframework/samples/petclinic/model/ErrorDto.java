package org.springframework.samples.petclinic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Credits to: Willie Wheeler
 * (http://springinpractice.com/2013/10/09/generating-json-error-object-responses-with-spring-web-mvc)
 *
 * @author Willie Wheeler (@williewheeler)
 * @author Nils Hartmann
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDto {
  @Getter @Setter private String code;
  @Getter @Setter private String message;
  private List<String> globalErrors;
  private Map<String, FieldErrorDto> fieldErrors;

  public ErrorDto() {}

  public ErrorDto(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public Map<String, FieldErrorDto> getFieldErrors() {
    return fieldErrors;
  }

  public void setFieldErrors(List<FieldErrorDto> fieldErrors) {
    this.fieldErrors = new LinkedHashMap<>();
    for (FieldErrorDto fieldError : fieldErrors) {
      this.fieldErrors.put(fieldError.getField(), fieldError);
    }
  }

  public void addGlobalError(String message) {
    if (globalErrors == null) {
      globalErrors = new LinkedList<>();
    }

    globalErrors.add(message);
  }
}
