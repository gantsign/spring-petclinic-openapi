/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web.api;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.mapper.MappingValidationException;
import org.springframework.samples.petclinic.mapper.PetMapper;
import org.springframework.samples.petclinic.mapper.PetTypeMapper;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetDto;
import org.springframework.samples.petclinic.model.PetFieldsDto;
import org.springframework.samples.petclinic.model.PetTypeDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** @author Nils Hartmann */
@Slf4j
@RequiredArgsConstructor
@RestController
public class PetApiController extends AbstractResourceController {

  private final ClinicService clinicService;
  private final PetTypeMapper petTypeMapper;
  private final PetMapper petMapper;

  @GetMapping("/pettypes")
  public List<PetTypeDto> getPetTypes() {
    return clinicService.findPetTypes().stream()
        .map(petTypeMapper::petTypeToPetTypeDto)
        .collect(toList());
  }

  @SuppressWarnings("IfCanBeAssertion")
  @PostMapping("/owners/{ownerId}/pets")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void addNewPet(
      @PathVariable("ownerId") final int ownerId,
      @RequestBody final @Valid PetFieldsDto petFieldsDto) {

    log.info("PetFieldsDto: {}", petFieldsDto);

    Pet pet = new Pet();
    Owner owner = clinicService.findOwnerById(ownerId);
    if (owner == null) {
      throw new ResponseStatusException(NOT_FOUND, "Owner with ID '" + ownerId + "' is unknown.");
    }
    owner.addPet(pet);

    save(pet, petFieldsDto);
  }

  @PutMapping("/owners/{ownerId}/pets/{petId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void processUpdateForm(
      @PathVariable("petId") final int petId, @RequestBody final @Valid PetFieldsDto petFieldsDto) {

    save(clinicService.findPetById(petId), petFieldsDto);
  }

  private void save(Pet pet, PetFieldsDto petCoreFieldsDto) {

    try {
      petMapper.updatePetFromPetFieldsDto(pet, petCoreFieldsDto);
    } catch (MappingValidationException e) {
      throw new ResponseStatusException(UNPROCESSABLE_ENTITY, e.getMessage(), e);
    }

    clinicService.savePet(pet);
  }

  @GetMapping("/owners/*/pets/{petId}")
  public PetDto findPet(@PathVariable("petId") int petId) {
    final Pet pet = clinicService.findPetById(petId);

    return petMapper.petToPetDto(pet);
  }

  // @Getter
  // static class PetDetails {
  //
  // long id;
  // String name;
  // String owner;
  // @DateTimeFormat(pattern = "yyyy-MM-dd")
  // Date birthDate;
  // PetType type;
  //
  // PetDetails(Pet pet) {
  // this.id = pet.getId();
  // this.name = pet.getName();
  // this.owner = pet.getOwner().getFirstName() + " " +
  // pet.getOwner().getLastName();
  // this.birthDate = pet.getBirthDate();
  // this.type = pet.getType();
  // }
  //
  // }

}
