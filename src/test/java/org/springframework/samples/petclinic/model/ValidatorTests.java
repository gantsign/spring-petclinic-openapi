package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Simple test to make sure that Bean Validation is working (useful when upgrading to a new version
 * of Hibernate Validator/ Bean Validation)
 *
 * @author Michael Isvy
 */
public class ValidatorTests {

  private static Validator createValidator() {
    LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
    localValidatorFactoryBean.afterPropertiesSet();
    return localValidatorFactoryBean;
  }

  @Test
  public void shouldNotValidateWhenFirstNameEmpty() {

    LocaleContextHolder.setLocale(Locale.ENGLISH);
    OwnerFieldsDto owner =
        new OwnerFieldsDto()
            .firstName("")
            .lastName("smith")
            .address("1 Station Rd.")
            .city("London")
            .telephone("012345");

    Validator validator = createValidator();
    Set<ConstraintViolation<OwnerFieldsDto>> constraintViolations = validator.validate(owner);

    assertThat(constraintViolations.size()).isEqualTo(1);
    ConstraintViolation<OwnerFieldsDto> violation = constraintViolations.iterator().next();
    assertThat(violation.getPropertyPath().toString()).isEqualTo("firstName");
    assertThat(violation.getMessage()).isEqualTo("size must be between 1 and 30");
  }
}
