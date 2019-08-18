package org.springframework.samples.petclinic.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetRequestDto {
  private Integer id;

  private @NotNull LocalDate birthDate;

  private @Size(min = 2, max = 14) String name;

  private Integer typeId;

  @JsonProperty("isNew")
  public boolean isNew() {
    return id == null;
  }

  @Override
  public String toString() {
    return "PetRequest [id="
        + id
        + ", birthDate="
        + birthDate
        + ", name="
        + name
        + ", typeId="
        + typeId
        + ']';
  }
}
