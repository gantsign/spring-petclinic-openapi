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

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.CacheControl.maxAge;
import static org.springframework.http.CacheControl.noCache;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.MappingValidationException;
import org.springframework.samples.petclinic.mapper.PetMapper;
import org.springframework.samples.petclinic.mapper.PetTypeMapper;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetDto;
import org.springframework.samples.petclinic.model.PetFieldsDto;
import org.springframework.samples.petclinic.model.PetTypeDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

/**
 * REST controller for pet related endpoints.
 *
 * @author Nils Hartmann
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("${openapi.springPetClinic.base-path:/api}")
public class PetApiController implements PetApi {

  private final ClinicService clinicService;
  private final PetTypeMapper petTypeMapper;
  private final PetMapper petMapper;

  @Override
  public ResponseEntity<List<PetTypeDto>> listPetTypes(String ifNoneMatch) {
    return clinicService.findPetTypes().stream()
        .map(petTypeMapper::petTypeToPetTypeDto)
        .collect(
            collectingAndThen(
                toList(),
                body ->
                    ResponseEntity.status(OK)
                        .cacheControl(maxAge(Duration.ofMinutes(5)))
                        .body(body)));
  }

  @SuppressWarnings("IfCanBeAssertion")
  @Override
  public ResponseEntity<Void> addPet(Integer ownerId, PetFieldsDto petFieldsDto) {

    log.info("PetFieldsDto: {}", petFieldsDto);

    Pet pet = new Pet();
    Owner owner = clinicService.findOwnerById(ownerId);
    if (owner == null) {
      throw new ResponseStatusException(NOT_FOUND, "Owner with ID '" + ownerId + "' is unknown.");
    }
    owner.addPet(pet);

    save(pet, petFieldsDto);

    return ResponseEntity.status(CREATED).build();
  }

  @SuppressWarnings("IfCanBeAssertion")
  @Override
  public ResponseEntity<Void> updatePet(Integer ownerId, Integer petId, PetFieldsDto petFieldsDto) {
    final Pet pet = clinicService.findPetByIdAndOwnerId(petId, ownerId);
    if (pet == null) {
      throw new ResponseStatusException(
          NOT_FOUND, "Pet with ID '" + petId + "' and Owner ID '" + ownerId + "' is unknown.");
    }

    save(pet, petFieldsDto);

    return ResponseEntity.noContent().build();
  }

  private void save(Pet pet, PetFieldsDto petCoreFieldsDto) {

    try {
      petMapper.updatePetFromPetFieldsDto(pet, petCoreFieldsDto);
    } catch (MappingValidationException e) {
      throw new ResponseStatusException(UNPROCESSABLE_ENTITY, e.getMessage(), e);
    }

    clinicService.savePet(pet);
  }

  @SuppressWarnings("IfCanBeAssertion")
  @Override
  public ResponseEntity<PetDto> getPet(Integer ownerId, Integer petId, String ifNoneMatch) {
    final Pet pet = clinicService.findPetByIdAndOwnerId(petId, ownerId);
    if (pet == null) {
      throw new ResponseStatusException(
          NOT_FOUND, "Pet with ID '" + petId + "' and Owner ID '" + ownerId + "' is unknown.");
    }

    return ResponseEntity.status(OK).cacheControl(noCache()).body(petMapper.petToPetDto(pet));
  }
}
