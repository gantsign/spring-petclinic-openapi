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
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Simple JavaBean domain object representing a visit.
 *
 * @author Ken Krebs
 */
@Getter
@Setter
@Entity
@Table(name = "visits")
public class Visit extends BaseEntity {

  /** Holds value of property date. */
  @Column(name = "visit_date")
  private LocalDate date;

  /** Holds value of property description. */
  @Column(name = "description")
  private @NotEmpty String description;

  /** Holds value of property pet. */
  @ManyToOne
  @JoinColumn(name = "pet_id")
  @JsonIgnore
  private Pet pet;

  /** Creates a new instance of Visit for the current date */
  public Visit() {
    date = LocalDate.now();
  }
}
