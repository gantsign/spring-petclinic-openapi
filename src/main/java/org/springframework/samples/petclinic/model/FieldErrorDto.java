package org.springframework.samples.petclinic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * Credits to: Willie Wheeler
 * (http://springinpractice.com/2013/10/09/generating-json-error-object-responses-with-spring-web-mvc)
 *
 * @author Willie Wheeler (@williewheeler)
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldErrorDto {
  private String resource;
  private String field;
  private String code;
  private String message;
}
