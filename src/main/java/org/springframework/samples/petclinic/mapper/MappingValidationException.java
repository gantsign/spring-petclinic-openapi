package org.springframework.samples.petclinic.mapper;

public class MappingValidationException extends RuntimeException {

  public MappingValidationException(String message) {
    super(message);
  }
}
