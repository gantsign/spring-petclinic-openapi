package org.springframework.samples.petclinic.web.api;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.IntStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.mapper.PetMapper;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetDto;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class PetApiControllerTests extends TestBase {

  @Autowired WebTestClient webTestClient;

  @Autowired PetMapper petMapper;

  @MockBean ClinicService clinicService;

  @Test
  public void shouldGetPetByPetIdAndOwnerId() throws Exception {

    Pet pet = setupPet();

    when(clinicService.findPetByIdAndOwnerId(2, 1)).thenReturn(pet);

    webTestClient
        .get()
        .uri("/api/owner/1/pet/2")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id")
        .isEqualTo(2)
        .jsonPath("$.name")
        .isEqualTo("Basil")
        .jsonPath("$.birthDate")
        .isEqualTo("2019-08-22")
        .jsonPath("$.typeId")
        .isEqualTo(6)
        .jsonPath("$.type.id")
        .isEqualTo(6)
        .jsonPath("$.type.name")
        .isEqualTo("hamster");
  }

  @Test
  public void shouldReturnNotFoundWhenFetchingUnknownPet() throws Exception {

    when(clinicService.findPetByIdAndOwnerId(4040, 404)).thenReturn(null);

    webTestClient
        .get()
        .uri("/api/owner/404/pet/4040")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.message")
        .isEqualTo("Pet with ID '4040' and Owner ID '404' is unknown.");
  }

  @Test
  public void shouldAddPet() throws Exception {

    Pet newPet = setupPet();
    PetDto petDto = petMapper.petToPetDto(newPet);

    when(clinicService.findOwnerById(1)).thenReturn(newPet.getOwner());
    when(clinicService.findPetTypes()).thenReturn(singletonList(newPet.getType()));

    String petAsJsonString = objectMapper.writeValueAsString(petDto);

    webTestClient
        .post()
        .uri("/api/owner/1/pet")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(petAsJsonString)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody()
        .isEmpty();

    ArgumentCaptor<Pet> arg = ArgumentCaptor.forClass(Pet.class);
    verify(clinicService).savePet(arg.capture());
    Pet actualPet = arg.getValue();
    assertThat(actualPet.getOwner().getId()).isEqualTo(1);
    assertThat(actualPet.getOwner().getFirstName()).isEqualTo("George");
    assertThat(actualPet.getOwner().getLastName()).isEqualTo("Bush");
    assertThat(actualPet.getName()).isEqualTo("Basil");
    assertThat(actualPet.getBirthDate()).isEqualTo(LocalDate.of(2019, 8, 22));
    assertThat(actualPet.getType().getId()).isEqualTo(6);
  }

  @Test
  public void shouldReturnNotFoundWhenAddingForUnknownOwner() throws Exception {

    when(clinicService.findOwnerById(1)).thenReturn(null);

    Pet newPet = setupPet();
    PetDto petDto = petMapper.petToPetDto(newPet);
    String petAsJsonString = objectMapper.writeValueAsString(petDto);

    webTestClient
        .post()
        .uri("/api/owner/1/pet")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(petAsJsonString)
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.message")
        .isEqualTo("Owner with ID '1' is unknown.");
  }

  @Test
  public void shouldReturnUnprocessableEntityWhenAddingWithUnknownType() throws Exception {

    Pet newPet = setupPet();
    PetDto petDto = petMapper.petToPetDto(newPet);

    when(clinicService.findOwnerById(1)).thenReturn(newPet.getOwner());
    when(clinicService.findPetTypes()).thenReturn(emptyList());

    String petAsJsonString = objectMapper.writeValueAsString(petDto);

    webTestClient
        .post()
        .uri("/api/owner/1/pet")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(petAsJsonString)
        .exchange()
        .expectStatus()
        .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
        .expectBody()
        .jsonPath("$.message")
        .isEqualTo("Unsupported petTypeId: 6");
  }

  @Test
  public void shouldUpdatePet() throws Exception {

    Pet newPet = setupPet();
    PetDto petDto = petMapper.petToPetDto(newPet);

    when(clinicService.findPetByIdAndOwnerId(2, 1)).thenReturn(newPet);
    when(clinicService.findPetTypes()).thenReturn(singletonList(newPet.getType()));

    petDto.setName("NewName");
    String petAsJsonString = objectMapper.writeValueAsString(petDto);

    webTestClient
        .put()
        .uri("/api/owner/1/pet/2")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(petAsJsonString)
        .exchange()
        .expectStatus()
        .isNoContent()
        .expectBody()
        .isEmpty();

    ArgumentCaptor<Pet> arg = ArgumentCaptor.forClass(Pet.class);
    verify(clinicService).savePet(arg.capture());
    Pet actualPet = arg.getValue();
    assertThat(actualPet.getOwner().getId()).isEqualTo(1);
    assertThat(actualPet.getOwner().getFirstName()).isEqualTo("George");
    assertThat(actualPet.getOwner().getLastName()).isEqualTo("Bush");
    assertThat(actualPet.getName()).isEqualTo("NewName");
    assertThat(actualPet.getBirthDate()).isEqualTo(LocalDate.of(2019, 8, 22));
    assertThat(actualPet.getType().getId()).isEqualTo(6);
  }

  @Test
  public void shouldReturnNotFoundWhenUpdatingForUnknownOwner() throws Exception {

    when(clinicService.findPetByIdAndOwnerId(2, 1)).thenReturn(null);

    Pet newPet = setupPet();
    PetDto petDto = petMapper.petToPetDto(newPet);
    String petAsJsonString = objectMapper.writeValueAsString(petDto);

    webTestClient
        .put()
        .uri("/api/owner/1/pet/2")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(petAsJsonString)
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.message")
        .isEqualTo("Pet with ID '2' and Owner ID '1' is unknown.");
  }

  @Test
  public void shouldListPetTypes() throws Exception {

    when(clinicService.findPetTypes()).thenReturn(setupPetTypes());

    webTestClient
        .get()
        .uri("/api/pet-type")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.length()")
        .isEqualTo(2)
        .jsonPath("$[0].id")
        .isEqualTo(1)
        .jsonPath("$[0].name")
        .isEqualTo("type1")
        .jsonPath("$[1].id")
        .isEqualTo(2)
        .jsonPath("$[1].name")
        .isEqualTo("type2");
  }

  private static Pet setupPet() {
    Owner owner = new Owner();
    owner.setId(1);
    owner.setFirstName("George");
    owner.setLastName("Bush");

    Pet pet = new Pet();

    pet.setName("Basil");
    pet.setId(2);
    pet.setBirthDate(LocalDate.of(2019, 8, 22));

    PetType petType = new PetType();
    petType.setId(6);
    petType.setName("hamster");
    pet.setType(petType);

    owner.addPet(pet);
    return pet;
  }

  private static Collection<PetType> setupPetTypes() {
    return IntStream.rangeClosed(1, 2)
        .mapToObj(
            value -> {
              PetType petType = new PetType();
              petType.setId(value);
              petType.setName("type" + value);
              return petType;
            })
        .collect(toList());
  }
}
