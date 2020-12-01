package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class NamedEntityTests {

  @Test
  public void shouldReturnToString() {
    Specialty specialty = new Specialty();
    specialty.setName("spec1");

    assertThat(specialty).hasToString("spec1");
  }
}
