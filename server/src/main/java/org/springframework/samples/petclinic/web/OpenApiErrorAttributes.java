package org.springframework.samples.petclinic.web;

import com.atlassian.oai.validator.springmvc.InvalidRequestException;
import com.atlassian.oai.validator.springmvc.InvalidResponseException;
import java.util.Collections;
import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Component
public class OpenApiErrorAttributes extends DefaultErrorAttributes {

  @Override
  public Map<String, Object> getErrorAttributes(WebRequest request, ErrorAttributeOptions options) {
    Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);

    Throwable throwable = getError(request);
    if (throwable instanceof InvalidRequestException) {
      errorAttributes.put("message", "Request failed schema validation");
      errorAttributes.put(
          "schemaValidationErrors",
          ((InvalidRequestException) throwable).getValidationReport().getMessages());
    } else if (throwable instanceof InvalidResponseException) {
      errorAttributes.put("message", "Response failed schema validation");
      errorAttributes.put(
          "schemaValidationErrors",
          ((InvalidResponseException) throwable).getValidationReport().getMessages());
    } else {
      errorAttributes.put("schemaValidationErrors", Collections.emptyList());
    }

    return errorAttributes;
  }
}
