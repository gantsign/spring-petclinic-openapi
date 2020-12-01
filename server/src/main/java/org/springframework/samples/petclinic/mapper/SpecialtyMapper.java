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
import org.springframework.lang.Nullable;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.SpecialtyDto;

@Mapper
public interface SpecialtyMapper {

  @Nullable
  @Contract("null -> null; !null -> !null")
  SpecialtyDto specialtyToSpecialtyDto(@Nullable Specialty specialty);

  /** Converts a set of Specialties to a list of SpecialtyDtos sorted by name. */
  default List<@NotNull SpecialtyDto> specialtiesToSpecialtyDtos(
      @Nullable Set<@NotNull Specialty> specialties) {
    specialties = firstNonNull(specialties, emptySet());
    return specialties.stream()
        .map(Objects::requireNonNull)
        .map(this::specialtyToSpecialtyDto)
        .sorted(comparing(SpecialtyDto::getName, nullsFirst(String.CASE_INSENSITIVE_ORDER)))
        .collect(collectingAndThen(toList(), Collections::unmodifiableList));
  }
}
