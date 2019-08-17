package org.springframework.samples.petclinic.web.api;

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
public class ErrorResource {
  @Getter @Setter private String code;
  @Getter @Setter private String message;
  private List<String> globalErrors;
  private Map<String, FieldErrorResource> fieldErrors;

  public ErrorResource() {}

  public ErrorResource(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public Map<String, FieldErrorResource> getFieldErrors() {
    return fieldErrors;
  }

  public void setFieldErrors(List<FieldErrorResource> fieldErrors) {
    this.fieldErrors = new LinkedHashMap<>();
    for (FieldErrorResource fieldErrorResource : fieldErrors) {
      this.fieldErrors.put(fieldErrorResource.getField(), fieldErrorResource);
    }
  }

  public void addGlobalError(String message) {
    if (globalErrors == null) {
      globalErrors = new LinkedList<>();
    }

    globalErrors.add(message);
  }
}
