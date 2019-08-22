package org.springframework.samples.petclinic.web.api;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller used to showcase what happens when an exception is thrown.
 *
 * @author NilsHartmann
 */
@Controller
@RequestMapping("${openapi.springPetClinic.base-path:/api}")
public class FailingApiController implements FailingApi {

  @Override
  public ResponseEntity<String> failingRequest() {
    throw new ResponseStatusException(
        BAD_REQUEST,
        "Expected: controller used to showcase what happens when an exception is thrown");
  }
}
