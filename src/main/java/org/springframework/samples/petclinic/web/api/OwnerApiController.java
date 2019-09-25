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
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.MappingValidationException;
import org.springframework.samples.petclinic.mapper.OwnerMapper;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.OwnerDto;
import org.springframework.samples.petclinic.model.OwnerFieldsDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

/**
 * REST controller for owner related endpoints.
 *
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("${openapi.springPetClinic.base-path:/api}")
public class OwnerApiController implements OwnerApi {

  private final ClinicService clinicService;
  private final OwnerMapper ownerMapper;

  @SuppressWarnings("IfCanBeAssertion")
  private Owner retrieveOwner(int ownerId) {
    Owner owner = clinicService.findOwnerById(ownerId);
    if (owner == null) {
      throw new ResponseStatusException(NOT_FOUND, "Owner with ID '" + ownerId + "' is unknown.");
    }
    return owner;
  }

  /** Create Owner. */
  @Override
  public ResponseEntity<OwnerDto> addOwner(OwnerFieldsDto ownerFieldsDto) {
    Owner owner;
    try {
      owner = ownerMapper.ownerFieldsDtoToOwner(ownerFieldsDto);
    } catch (MappingValidationException e) {
      throw new ResponseStatusException(UNPROCESSABLE_ENTITY, e.getMessage(), e);
    }

    clinicService.saveOwner(owner);

    return ResponseEntity.status(CREATED).body(ownerMapper.ownerToOwnerDto(owner));
  }

  /** Read single Owner. */
  @Override
  public ResponseEntity<OwnerDto> getOwner(Integer ownerId, String ifNoneMatch) {
    Owner owner = retrieveOwner(ownerId);
    return ResponseEntity.ok(ownerMapper.ownerToOwnerDto(owner));
  }

  /** Read List of Owners. */
  @Override
  public ResponseEntity<List<OwnerDto>> listOwners(String lastName, String ifNoneMatch) {

    if (lastName == null) {
      lastName = "";
    }

    return clinicService.findOwnerByLastName(lastName).stream()
        .map(ownerMapper::ownerToOwnerDto)
        .collect(collectingAndThen(toList(), ResponseEntity::ok));
  }

  /** Update Owner. */
  @Override
  public ResponseEntity<OwnerDto> updateOwner(Integer ownerId, OwnerFieldsDto ownerFieldsDto) {
    Owner ownerModel = retrieveOwner(ownerId);
    try {
      ownerMapper.updateOwnerFromOwnerFieldsDto(ownerModel, ownerFieldsDto);
    } catch (MappingValidationException e) {
      throw new ResponseStatusException(UNPROCESSABLE_ENTITY, e.getMessage(), e);
    }

    clinicService.saveOwner(ownerModel);
    return ResponseEntity.ok(ownerMapper.ownerToOwnerDto(ownerModel));
  }
}
