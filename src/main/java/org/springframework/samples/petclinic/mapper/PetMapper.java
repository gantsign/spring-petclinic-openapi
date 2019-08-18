package org.springframework.samples.petclinic.mapper;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetDto;
import org.springframework.samples.petclinic.model.PetFieldsDto;

@Mapper(uses = {PetTypeLookup.class, VisitMapper.class})
public interface PetMapper {

  @Mapping(target = "typeId", source = "type")
  PetDto petToPetDto(Pet pet);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "type", source = "typeId")
  @Mapping(target = "owner", ignore = true)
  @Mapping(target = "visits", ignore = true)
  void updatePetFromPetFieldsDto(@MappingTarget Pet pet, PetFieldsDto petFieldsDto);

  default List<PetDto> petsToPetDtos(Set<Pet> pets) {
    return pets.stream()
        .map(this::petToPetDto)
        .sorted(comparing(PetDto::getName, String.CASE_INSENSITIVE_ORDER))
        .collect(collectingAndThen(toList(), Collections::unmodifiableList));
  }
}
