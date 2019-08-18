package org.springframework.samples.petclinic.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.web.api.BadRequestException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetTypeLookup {

  private final ClinicService clinicService;

  public PetType fromPetTypeId(Integer petTypeId) {
    if (petTypeId == null) {
      return null;
    }
    return clinicService.findPetTypes().stream()
        .filter(petType -> petType.getId() == petTypeId.intValue())
        .findFirst()
        .orElseThrow(() -> new BadRequestException("Unsupported petTypeId: " + petTypeId));
  }

  public Integer toPetTypeId(PetType petType) {
    if (petType == null) {
      return null;
    }
    return petType.getId();
  }
}
