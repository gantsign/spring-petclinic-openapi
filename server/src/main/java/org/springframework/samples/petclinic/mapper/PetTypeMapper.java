package org.springframework.samples.petclinic.mapper;

import org.jetbrains.annotations.Contract;
import org.mapstruct.Mapper;
import org.springframework.lang.Nullable;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.PetTypeDto;

@Mapper
public interface PetTypeMapper {

  @Nullable
  @Contract("null -> null; !null -> !null")
  PetTypeDto petTypeToPetTypeDto(@Nullable PetType petType);
}
