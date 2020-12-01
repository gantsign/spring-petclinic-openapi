package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class OwnerTests {

  @Test
  public void shouldReturnToString() {
    Owner owner = new Owner();
    owner.setId(1);
    owner.setFirstName("firstName1");
    owner.setLastName("lastName1");
    owner.setAddress("address1");
    owner.setCity("city1");
    owner.setTelephone("01234");

    assertThat(owner.toString())
        .matches(
            "\\[Owner@[0-9a-f]+ id = 1, new = false, lastName = 'lastName1', "
                + "firstName = 'firstName1', address = 'address1', city = 'city1', "
                + "telephone = '01234']");
  }
}
