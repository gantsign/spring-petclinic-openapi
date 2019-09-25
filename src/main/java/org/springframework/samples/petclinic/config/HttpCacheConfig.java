package org.springframework.samples.petclinic.config;

import javax.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration
public class HttpCacheConfig {

  @Bean
  public Filter filter() {
    return new ShallowEtagHeaderFilter();
  }
}
