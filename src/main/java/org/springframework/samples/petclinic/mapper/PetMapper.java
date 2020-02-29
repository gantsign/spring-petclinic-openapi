package org.springframework.samples.petclinic.mapper;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.util.Collections.emptySet;
import static java.util.Comparator.comparing;
import static java.util.Comparator.nullsFirst;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.lang.Nullable;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetDto;
import org.springframework.samples.petclinic.model.PetFieldsDto;

@Mapper(uses = {PetTypeLookup.class, VisitMapper.class})
public interface PetMapper {

  @Mapping(target = "typeId", source = "type")
  @Nullable
  @Contract("null -> null; !null -> !null")
  PetDto petToPetDto(@Nullable Pet pet);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "type", source = "typeId")
  @Mapping(target = "owner", ignore = true)
  @Mapping(target = "visits", ignore = true)
  void updatePetFromPetFieldsDto(@MappingTarget Pet pet, @Nullable PetFieldsDto petFieldsDto);

  /** Converts a set of Pets to a list of PetDtos sorted by name. */
  default List<PetDto> petsToPetDtos(@Nullable Set<@NotNull Pet> pets) {
    pets = firstNonNull(pets, emptySet());
    return pets.stream()
        .map(Objects::requireNonNull)
        .map(this::petToPetDto)
        .sorted(comparing(PetDto::getName, nullsFirst(String.CASE_INSENSITIVE_ORDER)))
        .collect(collectingAndThen(toList(), Collections::unmodifiableList));
  }
}
