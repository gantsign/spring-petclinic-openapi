package org.springframework.samples.petclinic.web.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(
    controllers = PetApiController.class,
    includeFilters =
        @ComponentScan.Filter(
            type = FilterType.REGEX,
            pattern = "org\\.springframework\\.samples\\.petclinic\\.mapper\\..*"))
public class PetApiControllerTests {

  @Autowired private MockMvc mvc;

  @MockBean ClinicService clinicService;

  @Test
  public void shouldGetPetByPetIdAndOwnerId() throws Exception {

    Pet pet = setupPet();

    when(clinicService.findPetById(2)).thenReturn(pet);

    mvc.perform(get("/api/owners/2/pets/2").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(jsonPath("$.id").value(2))
        .andExpect(jsonPath("$.name").value("Basil"))
        .andExpect(jsonPath("$.typeId").value(6));
  }

  private static Pet setupPet() {
    Owner owner = new Owner();
    owner.setFirstName("George");
    owner.setLastName("Bush");

    Pet pet = new Pet();

    pet.setName("Basil");
    pet.setId(2);

    PetType petType = new PetType();
    petType.setId(6);
    pet.setType(petType);

    owner.addPet(pet);
    return pet;
  }
}
