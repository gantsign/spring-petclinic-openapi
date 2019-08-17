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

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Simple JavaBean domain object representing an person.
 *
 * @author Ken Krebs
 */
@Getter
@Setter
@MappedSuperclass
public class Person extends BaseEntity {

  @Column(name = "first_name")
  private @NotEmpty @Pattern(regexp = "[a-z-A-Z]*", message = "First name has invalid characters")
  String firstName;

  @Column(name = "last_name")
  protected @NotEmpty @Pattern(regexp = "[a-z-A-Z]*", message = "Last name has invalid characters")
  String lastName;
}
