package org.springframework.samples.petclinic.web.api;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller used to showcase what happens when an exception is thrown
 *
 * @author NilsHartmann
 */
@RestController
public class FailingApiController extends AbstractResourceController {

  @GetMapping("/oups")
  @ResponseBody
  String failingRequest() {
    throw new ResponseStatusException(
        BAD_REQUEST,
        "Expected: controller used to showcase what happens when an exception is thrown");
  }
}
