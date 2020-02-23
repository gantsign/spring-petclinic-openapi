package org.springframework.samples.petclinic.config;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/** Cache could be disable in unit test. */
@Configuration
@EnableCaching
@Profile("production")
public class CacheConfig {

  /** Configures Ehcache. */
  @SuppressWarnings({"MagicNumber", "resource"})
  @Bean
  public JCacheManagerCustomizer cacheManagerCustomizer() {
    return cacheManager -> {
      CacheConfiguration<Object, Object> config =
          CacheConfigurationBuilder.newCacheConfigurationBuilder(
                  Object.class,
                  Object.class,
                  ResourcePoolsBuilder.newResourcePoolsBuilder().heap(100, EntryUnit.ENTRIES))
              .withExpiry(
                  ExpiryPolicyBuilder.timeToLiveExpiration(Duration.of(60, ChronoUnit.SECONDS)))
              .build();
      cacheManager.createCache("vets", Eh107Configuration.fromEhcacheCacheConfiguration(config));
    };
  }
}
