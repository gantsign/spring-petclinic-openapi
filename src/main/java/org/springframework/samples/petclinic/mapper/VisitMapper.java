package org.springframework.samples.petclinic.mapper;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.model.VisitDto;
import org.springframework.samples.petclinic.model.VisitFieldsDto;

@Mapper
public interface VisitMapper {

  VisitDto visitToVisitDto(Visit visit);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "pet", ignore = true)
  Visit visitFieldsDtoToVisit(VisitFieldsDto visitFieldsDto);

  /** Converts a set of Visits to a list of VisitDtos sorted by date. */
  default List<VisitDto> visitsToVisitDtos(Set<Visit> visits) {
    return visits.stream()
        .map(this::visitToVisitDto)
        .sorted(comparing(VisitDto::getDate).reversed())
        .collect(collectingAndThen(toList(), Collections::unmodifiableList));
  }
}
