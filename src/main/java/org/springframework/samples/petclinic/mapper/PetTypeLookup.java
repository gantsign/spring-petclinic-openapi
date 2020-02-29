package org.springframework.samples.petclinic.mapper;

import static java.util.Objects.requireNonNull;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.springframework.lang.Nullable;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetTypeLookup {

  private final ClinicService clinicService;

  /** Returns the PetType for the specified ID. */
  @Contract("null -> null; !null -> !null")
  public @Nullable PetType fromPetTypeId(@Nullable Integer petTypeId) {
    if (petTypeId == null) {
      return null;
    }
    return clinicService.findPetTypes().stream()
        .filter(petType -> petTypeId.equals(petType.getId()))
        .findFirst()
        .orElseThrow(() -> new MappingValidationException("Unsupported petTypeId: " + petTypeId));
  }

  /** Returns the ID for the specified PetType. */
  @Contract("null -> null; !null -> !null")
  public @Nullable Integer toPetTypeId(@Nullable PetType petType) {
    if (petType == null) {
      return null;
    }
    return requireNonNull(petType.getId());
  }
}
