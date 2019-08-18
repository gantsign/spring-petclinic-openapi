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
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Simple business object representing a pet.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
@Getter
@Setter
@Entity
@Table(name = "pets")
public class Pet extends NamedEntity {

  @Column(name = "birth_date")
  private LocalDate birthDate;

  @ManyToOne
  @JoinColumn(name = "type_id")
  private PetType type;

  @ManyToOne
  @JoinColumn(name = "owner_id")
  @JsonIgnore
  private Owner owner;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", fetch = FetchType.EAGER)
  @JsonIgnore
  private @NonNull Set<Visit> visits = new HashSet<>();

  @JsonProperty("visits")
  public List<Visit> getVisitsSorted() {
    return visits.stream()
        .sorted(comparing(Visit::getDate).reversed())
        .collect(collectingAndThen(toList(), Collections::unmodifiableList));
  }

  public void addVisit(Visit visit) {
    visits.add(visit);
    visit.setPet(this);
  }
}
