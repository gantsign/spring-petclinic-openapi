package org.springframework.samples.petclinic.mapper;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.SpecialtyDto;

@Mapper
public interface SpecialtyMapper {

  SpecialtyDto specialtyToSpecialtyDto(Specialty specialty);

  /** Converts a set of Specialties to a list of SpecialtyDtos sorted by name. */
  default List<SpecialtyDto> specialtiesToSpecialtyDtos(Set<Specialty> specialties) {
    return specialties.stream()
        .map(this::specialtyToSpecialtyDto)
        .sorted(comparing(SpecialtyDto::getName, String.CASE_INSENSITIVE_ORDER))
        .collect(collectingAndThen(toList(), Collections::unmodifiableList));
  }
}
