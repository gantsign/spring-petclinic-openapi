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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.core.style.ToStringCreator;

/**
 * Simple JavaBean domain object representing an owner.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
@Getter
@Setter
@Entity
@Table(name = "owners")
public class Owner extends Person {
  @Column(name = "address")
  private @NotEmpty String address;

  @Column(name = "city")
  private @NotEmpty String city;

  @Column(name = "telephone")
  private @NotEmpty @Digits(fraction = 0, integer = 10) String telephone;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
  @JsonIgnore
  private @NonNull Set<Pet> pets = new HashSet<>();

  @JsonProperty("pets")
  public List<Pet> getPetsSorted() {
    List<Pet> sortedPets = new ArrayList<>(pets);
    PropertyComparator.sort(sortedPets, new MutableSortDefinition("name", true, true));
    return Collections.unmodifiableList(sortedPets);
  }

  public void addPet(Pet pet) {
    if (pet.isNew()) {
      pets.add(pet);
    }
    pet.setOwner(this);
  }

  @Override
  public String toString() {
    return new ToStringCreator(this)
        .append("id", getId())
        .append("new", isNew())
        .append("lastName", getLastName())
        .append("firstName", getFirstName())
        .append("address", address)
        .append("city", city)
        .append("telephone", telephone)
        .toString();
  }
}
