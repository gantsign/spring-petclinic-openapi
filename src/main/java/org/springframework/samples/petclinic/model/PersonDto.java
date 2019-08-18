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

import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

/** DTO representing an person. */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PersonDto extends BaseDto {

  private @NotEmpty @Pattern(regexp = "[a-z-A-Z]*", message = "First name has invalid characters")
  String firstName;

  private @NotEmpty @Pattern(regexp = "[a-z-A-Z]*", message = "Last name has invalid characters")
  String lastName;
}
