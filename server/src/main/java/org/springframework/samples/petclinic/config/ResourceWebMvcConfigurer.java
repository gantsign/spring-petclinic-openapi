package org.springframework.samples.petclinic.config;

import java.io.IOException;
import java.time.Duration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class ResourceWebMvcConfigurer implements WebMvcConfigurer {

  private static final String META_INF_WEB = "classpath:META-INF/web/";
  private static final Duration THIRTY_DAYS = Duration.ofDays(30);
  private static final Duration EIGHT_HOURS = Duration.ofHours(8);

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("/static/**")
        .addResourceLocations(META_INF_WEB + "static/")
        .setCacheControl(CacheControl.maxAge(THIRTY_DAYS).mustRevalidate().cachePublic());

    registry
        .addResourceHandler("/images/**")
        .addResourceLocations(META_INF_WEB + "images/")
        .setCacheControl(CacheControl.maxAge(EIGHT_HOURS).mustRevalidate().cachePublic());

    registry
        .addResourceHandler("/**/*")
        .addResourceLocations(META_INF_WEB)
        .setCacheControl(CacheControl.noCache().mustRevalidate().cachePublic())
        .resourceChain(true)
        .addResolver(
            new PathResourceResolver() {
              @Override
              protected Resource getResource(String resourcePath, Resource location)
                  throws IOException {
                Resource resource = location.createRelative(resourcePath);
                return resource.exists() ? resource : location.createRelative("index.html");
              }
            });
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/").setViewName("forward:index.html");
  }
}
