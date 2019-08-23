package org.springframework.samples.petclinic.config;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.atlassian.oai.validator.springmvc.OpenApiValidationFilter;
import com.atlassian.oai.validator.springmvc.OpenApiValidationInterceptor;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Slf4j
@Configuration
public class OpenApiValidationConfig extends WebMvcConfigurerAdapter {
  private final OpenApiValidationInterceptor validationInterceptor;

  /** Creates an instance of OpenApiValidationConfig that permits all OPTIONS requests. */
  public OpenApiValidationConfig(
      @Value("classpath:schema/petclinic.openapi.yaml") final Resource apiSpecification)
      throws IOException {

    final EncodedResource specResource = new EncodedResource(apiSpecification, UTF_8);
    validationInterceptor =
        new OpenApiValidationInterceptor(specResource) {
          @SuppressWarnings("ProhibitedExceptionDeclared")
          @Override
          public boolean preHandle(
              HttpServletRequest servletRequest,
              HttpServletResponse servletResponse,
              Object handler)
              throws Exception {
            if (HttpMethod.OPTIONS.matches(servletRequest.getMethod())) {
              log.debug("Skipping OpenAPI request validation for OPTIONS request");
              return true;
            }
            return super.preHandle(servletRequest, servletResponse, handler);
          }

          @SuppressWarnings("ProhibitedExceptionDeclared")
          @Override
          public void postHandle(
              HttpServletRequest servletRequest,
              HttpServletResponse servletResponse,
              Object handler,
              ModelAndView modelAndView)
              throws Exception {
            if (HttpMethod.OPTIONS.matches(servletRequest.getMethod())) {
              log.debug("Skipping OpenAPI request validation for OPTIONS request");
              return;
            }
            super.postHandle(servletRequest, servletResponse, handler, modelAndView);
          }
        };
  }

  @Bean
  public Filter validationFilter() {
    return new OpenApiValidationFilter(
        true, // enable request validation
        true // enable response validation
        ) {
      @Override
      protected void doFilterInternal(
          HttpServletRequest servletRequest,
          HttpServletResponse servletResponse,
          FilterChain filterChain)
          throws ServletException, IOException {
        if (HttpMethod.OPTIONS.matches(servletRequest.getMethod())) {
          log.debug("Skipping OpenAPI request validation for OPTIONS request");
          filterChain.doFilter(servletRequest, servletResponse);
        } else {
          super.doFilterInternal(servletRequest, servletResponse, filterChain);
        }
      }
    };
  }

  @Override
  public void addInterceptors(final InterceptorRegistry registry) {
    registry.addInterceptor(validationInterceptor);
  }
}
