package org.springframework.samples.petclinic.web.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(
    controllers = VetApiController.class,
    includeFilters =
        @ComponentScan.Filter(
            type = FilterType.REGEX,
            pattern = "org\\.springframework\\.samples\\.petclinic\\.mapper\\..*"))
public class VetApiControllerTests {

  @Autowired private MockMvc mvc;

  @MockBean ClinicService clinicService;

  @Test
  public void shouldListVets() throws Exception {

    Vet vet = new Vet();
    vet.setId(1);

    when(clinicService.findVets()).thenReturn(Collections.singletonList(vet));

    mvc.perform(get("/api/vet").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1));
  }
}
