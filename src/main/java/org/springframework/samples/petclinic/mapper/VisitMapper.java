package org.springframework.samples.petclinic.mapper;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.util.Collections.emptySet;
import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
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
import org.springframework.lang.Nullable;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.model.VisitDto;
import org.springframework.samples.petclinic.model.VisitFieldsDto;

@Mapper
public interface VisitMapper {

  @Nullable
  @Contract("null -> null; !null -> !null")
  VisitDto visitToVisitDto(@Nullable Visit visit);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "pet", ignore = true)
  @Nullable
  @Contract("null -> null; !null -> !null")
  Visit visitFieldsDtoToVisit(@Nullable VisitFieldsDto visitFieldsDto);

  /** Converts a set of Visits to a list of VisitDtos sorted by date. */
  default List<@NotNull VisitDto> visitsToVisitDtos(@Nullable Set<@NotNull Visit> visits) {
    visits = firstNonNull(visits, emptySet());
    return visits.stream()
        .map(Objects::requireNonNull)
        .map(this::visitToVisitDto)
        .sorted(comparing(VisitDto::getDate, nullsFirst(naturalOrder())).reversed())
        .collect(collectingAndThen(toList(), Collections::unmodifiableList));
  }
}
