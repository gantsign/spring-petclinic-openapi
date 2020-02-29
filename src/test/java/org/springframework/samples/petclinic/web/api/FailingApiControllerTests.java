package org.springframework.samples.petclinic.web.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles({"test"})
public class FailingApiControllerTests extends TestBase {

  @Autowired WebTestClient webTestClient;

  @Test
  public void shouldReturnBadRequest() {
    webTestClient
        .get()
        .uri("/api/oops")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.status")
        .isEqualTo(400)
        .jsonPath("$.error")
        .isEqualTo("Bad Request")
        .jsonPath("$.path")
        .isEqualTo("/api/oops")
        .jsonPath("$.timestamp")
        .value(this::parseDateTime, String.class)
        .jsonPath("$.schemaValidationErrors.length()")
        .isEqualTo(0)
        .jsonPath("$.message")
        .isEqualTo(
            "Expected: controller used to showcase what happens when an exception is thrown");
  }
}
