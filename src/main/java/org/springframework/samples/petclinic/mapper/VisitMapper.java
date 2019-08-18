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

@Mapper
public interface VisitMapper {

  VisitDto visitToVisitDto(Visit visit);

  @Mapping(target = "pet", ignore = true)
  Visit visitDtoToVisit(VisitDto visitDto);

  default List<VisitDto> visitsToVisitDtos(Set<Visit> visits) {
    return visits.stream()
        .map(this::visitToVisitDto)
        .sorted(comparing(VisitDto::getDate).reversed())
        .collect(collectingAndThen(toList(), Collections::unmodifiableList));
  }
}
