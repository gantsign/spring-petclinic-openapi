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

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.MappingValidationException;
import org.springframework.samples.petclinic.mapper.VisitMapper;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.model.VisitFieldsDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

/**
 * REST controller for visit related endpoints.
 *
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("${openapi.springPetClinic.base-path:/api}")
public class VisitApiController implements VisitApi {

  private final ClinicService clinicService;
  private final VisitMapper visitMapper;

  @SuppressWarnings("IfCanBeAssertion")
  @Override
  public ResponseEntity<Void> addVisit(
      Integer ownerId, Integer petId, VisitFieldsDto visitFieldsDto) {
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

    return ResponseEntity.status(CREATED).build();
  }
}
