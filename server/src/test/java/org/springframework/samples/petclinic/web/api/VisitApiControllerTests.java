package org.springframework.samples.petclinic.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.mapper.VisitMapper;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.model.VisitDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class VisitApiControllerTests extends TestBase {

  private static final LocalDate DATE_2019_8_22 = LocalDate.of(2019, 8, 22);
  private static final LocalDate DATE_2019_8_23 = LocalDate.of(2019, 8, 23);

  @Autowired WebTestClient webTestClient;

  @Autowired VisitMapper visitMapper;

  @MockBean ClinicService clinicService;

  @Test
  public void shouldAddVisit() throws Exception {

    Visit newVisit = setupVisit();
    VisitDto visitDto = visitMapper.visitToVisitDto(newVisit);

    when(clinicService.findPetByIdAndOwnerId(2, 1)).thenReturn(newVisit.getPet());

    String visitAsJsonString = objectMapper.writeValueAsString(visitDto);

    webTestClient
        .post()
        .uri("/api/owner/1/pet/2/visit")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(visitAsJsonString)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody()
        .isEmpty();

    ArgumentCaptor<Visit> arg = ArgumentCaptor.forClass(Visit.class);
    verify(clinicService).saveVisit(arg.capture());

    assertThat(arg.getValue())
        .extracting(
            "pet.owner.id",
            "pet.owner.firstName",
            "pet.owner.lastName",
            "pet.name",
            "pet.birthDate",
            "pet.type.id",
            "date",
            "description")
        .containsExactly(1, "George", "Bush", "Basil", DATE_2019_8_22, 6, DATE_2019_8_23, "desc1");
  }

  @Test
  public void shouldReturnNotFoundWhenAddingForUnknownOwnerOrPet() throws Exception {

    when(clinicService.findPetByIdAndOwnerId(2, 1)).thenReturn(null);

    Visit newVisit = setupVisit();
    VisitDto visitDto = visitMapper.visitToVisitDto(newVisit);
    String visitAsJsonString = objectMapper.writeValueAsString(visitDto);

    webTestClient
        .post()
        .uri("/api/owner/1/pet/2/visit")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(visitAsJsonString)
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.message")
        .isEqualTo("Pet with ID '2' and Owner ID '1' is unknown.");
  }

  private static Visit setupVisit() {
    Owner owner = new Owner();
    owner.setId(1);
    owner.setFirstName("George");
    owner.setLastName("Bush");

    Pet pet = new Pet();

    pet.setName("Basil");
    pet.setId(2);
    pet.setBirthDate(DATE_2019_8_22);

    PetType petType = new PetType();
    petType.setId(6);
    petType.setName("hamster");
    pet.setType(petType);

    owner.addPet(pet);

    Visit visit = new Visit();
    visit.setId(3);
    visit.setDate(DATE_2019_8_23);
    visit.setDescription("desc1");
    pet.addVisit(visit);

    return visit;
  }
}
