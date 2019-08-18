package org.springframework.samples.petclinic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.OwnerDto;

@Mapper(uses = PetMapper.class)
public interface OwnerMapper {

  OwnerDto ownerToOwnerDto(Owner owner);

  @Mapping(target = "pets", ignore = true)
  Owner ownerDtoToOwner(OwnerDto ownerDto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "pets", ignore = true)
  void updateOwnerFromOwnerDto(@MappingTarget Owner owner, OwnerDto ownerDto);
}
