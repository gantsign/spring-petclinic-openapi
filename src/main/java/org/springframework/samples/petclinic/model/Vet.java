/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.model;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Simple JavaBean domain object representing a veterinarian.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Arjen Poutsma
 */
@Getter
@Setter
@Entity
@Table(name = "vets")
public class Vet extends Person {

  @JoinTable(
      name = "vet_specialties",
      joinColumns = @JoinColumn(name = "vet_id"),
      inverseJoinColumns = @JoinColumn(name = "specialty_id"))
  @ManyToMany(fetch = FetchType.EAGER)
  @JsonIgnore
  private @NonNull Set<Specialty> specialties = new HashSet<>();

  @JsonProperty("specialties")
  public List<Specialty> getSpecialtiesSorted() {
    return specialties.stream()
        .sorted(comparing(Specialty::getName, String.CASE_INSENSITIVE_ORDER))
        .collect(collectingAndThen(toList(), Collections::unmodifiableList));
  }

  @JsonIgnore
  public int getNrOfSpecialties() {
    return specialties.size();
  }
}
