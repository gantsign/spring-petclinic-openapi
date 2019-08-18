package org.springframework.samples.petclinic.mapper;

import org.mapstruct.Mapper;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.PetTypeDto;

@Mapper
public interface PetTypeMapper {

  PetTypeDto petTypeToPetTypeDto(PetType petType);
}
