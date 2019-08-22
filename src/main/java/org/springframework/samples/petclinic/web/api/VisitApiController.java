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

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.mapper.MappingValidationException;
import org.springframework.samples.petclinic.mapper.VisitMapper;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.model.VisitFieldsDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
public class VisitApiController extends AbstractResourceController {

  private final ClinicService clinicService;
  private final VisitMapper visitMapper;

  @SuppressWarnings("IfCanBeAssertion")
  @PostMapping("/owner/{ownerId}/pet/{petId}/visit")
  @ResponseStatus(HttpStatus.CREATED)
  public void createVisit(
      @PathVariable("ownerId") final int ownerId,
      @PathVariable("petId") int petId,
      @Valid @RequestBody VisitFieldsDto visitFieldsDto) {
    final Pet pet = clinicService.findPetByIdAndOwnerId(petId, ownerId);
    if (pet == null) {
      throw new ResponseStatusException(
          NOT_FOUND, "Pet with ID '" + petId + "' and Owner ID '" + ownerId + "' is unknown.");
    }

    Visit visit;
    try {
      visit = visitMapper.visitFieldsDtoToVisit(visitFieldsDto);
    } catch (MappingValidationException e) {
      throw new ResponseStatusException(UNPROCESSABLE_ENTITY, e.getMessage(), e);
    }
    pet.addVisit(visit);

    clinicService.saveVisit(visit);
  }
}
