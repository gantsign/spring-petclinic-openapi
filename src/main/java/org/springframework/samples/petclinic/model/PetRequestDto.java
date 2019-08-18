package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PetRequestDto extends NamedDto {
  private @NotNull LocalDate birthDate;
  private Integer typeId;
}
