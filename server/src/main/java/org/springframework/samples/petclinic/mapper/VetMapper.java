package org.springframework.samples.petclinic.mapper;

import org.jetbrains.annotations.Contract;
import org.mapstruct.Mapper;
import org.springframework.lang.Nullable;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.VetDto;

@Mapper(uses = SpecialtyMapper.class)
public interface VetMapper {

  @Nullable
  @Contract("null -> null; !null -> !null")
  VetDto vetToVetDto(@Nullable Vet vet);
}
