package org.springframework.samples.petclinic.mapper;

import org.mapstruct.Mapper;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.VetDto;

@Mapper(uses = SpecialtyMapper.class)
public interface VetMapper {

  VetDto vetToVetDto(Vet vet);
}
