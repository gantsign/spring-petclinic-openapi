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

import java.util.Collection;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.mapper.MappingValidationException;
import org.springframework.samples.petclinic.mapper.OwnerMapper;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.OwnerDto;
import org.springframework.samples.petclinic.model.OwnerFieldsDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@RequiredArgsConstructor
@RestController
public class OwnerApiController extends AbstractResourceController {

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

  /** Create Owner */
  @RequestMapping(value = "/owner", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  public OwnerDto createOwner(@RequestBody @Valid OwnerFieldsDto ownerFieldsDto) {
    Owner owner;
    try {
      owner = ownerMapper.ownerFieldsDtoToOwner(ownerFieldsDto);
    } catch (MappingValidationException e) {
      throw new ResponseStatusException(UNPROCESSABLE_ENTITY, e.getMessage(), e);
    }

    clinicService.saveOwner(owner);

    return ownerMapper.ownerToOwnerDto(owner);
  }

  /** Read single Owner */
  @RequestMapping(value = "/owner/{ownerId}", method = RequestMethod.GET)
  public OwnerDto findOwner(@PathVariable("ownerId") int ownerId) {
    Owner owner = retrieveOwner(ownerId);
    return ownerMapper.ownerToOwnerDto(owner);
  }

  /** Read List of Owners */
  @RequestMapping(value = "/owner/list", method = RequestMethod.GET)
  public Collection<OwnerDto> findOwnerCollection(@RequestParam("lastName") String ownerLastName) {

    if (ownerLastName == null) {
      ownerLastName = "";
    }

    return clinicService.findOwnerByLastName(ownerLastName).stream()
        .map(ownerMapper::ownerToOwnerDto)
        .collect(toList());
  }

  /** Update Owner */
  @RequestMapping(value = "/owner/{ownerId}", method = RequestMethod.PUT)
  public OwnerDto updateOwner(
      @PathVariable("ownerId") int ownerId, @Valid @RequestBody OwnerFieldsDto ownerFieldsDto) {
    Owner ownerModel = retrieveOwner(ownerId);
    try {
      ownerMapper.updateOwnerFromOwnerFieldsDto(ownerModel, ownerFieldsDto);
    } catch (MappingValidationException e) {
      throw new ResponseStatusException(UNPROCESSABLE_ENTITY, e.getMessage(), e);
    }

    clinicService.saveOwner(ownerModel);
    return ownerMapper.ownerToOwnerDto(ownerModel);
  }
}
