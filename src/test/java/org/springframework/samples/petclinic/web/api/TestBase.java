package org.springframework.samples.petclinic.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;

abstract class TestBase {

  @Autowired ObjectMapper objectMapper;

  void parseDateTime(String dateTime) {
    try {
      objectMapper.readValue('"' + dateTime + '"', Instant.class);
    } catch (IOException e) {
      throw new AssertionError("Unable to parse date-time: " + dateTime, e);
    }
  }
}
