package org.springframework.samples.petclinic.web.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    throw new SuperFatalErrorException(
        "Expected: controller used to showcase what " + "happens when an exception is thrown");
  }
}
