package org.springframework.samples.petclinic.mapper;

import org.jetbrains.annotations.Contract;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.lang.Nullable;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.OwnerDto;
import org.springframework.samples.petclinic.model.OwnerFieldsDto;

@Mapper(uses = PetMapper.class)
public interface OwnerMapper {

  @Nullable
  @Contract("null -> null; !null -> !null")
  OwnerDto ownerToOwnerDto(@Nullable Owner owner);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "pets", ignore = true)
  @Nullable
  @Contract("null -> null; !null -> !null")
  Owner ownerFieldsDtoToOwner(@Nullable OwnerFieldsDto ownerFieldsDto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "pets", ignore = true)
  void updateOwnerFromOwnerFieldsDto(
      @MappingTarget Owner owner, @Nullable OwnerFieldsDto ownerFieldsDto);
}
