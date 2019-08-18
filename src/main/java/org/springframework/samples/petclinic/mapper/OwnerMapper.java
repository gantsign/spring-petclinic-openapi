package org.springframework.samples.petclinic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.OwnerDto;
import org.springframework.samples.petclinic.model.OwnerFieldsDto;

@Mapper(uses = PetMapper.class)
public interface OwnerMapper {

  OwnerDto ownerToOwnerDto(Owner owner);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "pets", ignore = true)
  Owner ownerFieldsDtoToOwner(OwnerFieldsDto ownerFieldsDto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "pets", ignore = true)
  void updateOwnerFromOwnerFieldsDto(@MappingTarget Owner owner, OwnerFieldsDto ownerFieldsDto);
}
