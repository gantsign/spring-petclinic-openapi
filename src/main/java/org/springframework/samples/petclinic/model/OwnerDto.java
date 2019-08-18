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

import java.util.Collections;
import java.util.List;
import javax.validation.constraints.Digits;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

/** DTO representing an owner. */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OwnerDto extends PersonDto {
  private @NotEmpty String address;
  private @NotEmpty String city;
  private @NotEmpty @Digits(fraction = 0, integer = 10) String telephone;
  private @NonNull List<PetDto> pets = Collections.emptyList();
}