package org.springframework.samples.petclinic;

import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.simpleNameEndingWith;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.type;
import static com.tngtech.archunit.core.domain.JavaMember.Predicates.declaredIn;
import static com.tngtech.archunit.core.domain.properties.CanBeAnnotated.Predicates.annotatedWith;
import static com.tngtech.archunit.core.domain.properties.HasName.Predicates.name;
import static com.tngtech.archunit.core.domain.properties.HasName.Predicates.nameMatching;
import static com.tngtech.archunit.core.domain.properties.HasOwner.Predicates.With.owner;
import static com.tngtech.archunit.core.domain.properties.HasParameterTypes.Predicates.rawParameterTypes;
import static com.tngtech.archunit.core.domain.properties.HasReturnType.Predicates.rawReturnType;
import static com.tngtech.archunit.lang.conditions.ArchConditions.accessClassesThat;
import static com.tngtech.archunit.lang.conditions.ArchConditions.callMethodWhere;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noCodeUnits;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noFields;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.base.PackageMatcher;
import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaCall;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mapstruct.Mapper;
import org.springframework.lang.Nullable;

@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = "org.springframework.samples.petclinic")
public class ArchitectureTests {

  private static final ArchCondition<JavaClass> USE_NON_LANG3_STRING_UTILS =
      accessClassesThat(
              nameMatching(".*\\.StringUtils?")
                  .and(not(nameMatching("org\\.apache\\.commons\\.lang3\\..*"))))
          .as("use StringUtil/StringUtils other than Apache Commons Lang 3");

  private static final DescribedPredicate<JavaMethod> HAS_DTO_PARAMETER_TYPE =
      rawParameterTypes(
              new DescribedPredicate<List<JavaClass>>("has dto parameter") {
                final PackageMatcher packageMatcher = PackageMatcher.of("..model");

                @Override
                public boolean apply(List<JavaClass> input) {

                  if (input.size() != 1) {
                    return false;
                  }

                  JavaClass javaClass = input.get(0);
                  return packageMatcher.matches(javaClass.getPackageName())
                      && javaClass.getName().endsWith("Dto");
                }
              })
          .forSubType();

  private static final DescribedPredicate<JavaMethod> HAS_ENTITY_RETURN_TYPE =
      rawReturnType(resideInAnyPackage("..model").and(not(simpleNameEndingWith("Dto"))))
          .as("has entity return type")
          .forSubType();

  private static final DescribedPredicate<JavaMethod> HAS_ENTITY_PARAMETER_TYPE =
      rawParameterTypes(
              new DescribedPredicate<List<JavaClass>>("has entity parameter") {
                final PackageMatcher packageMatcher = PackageMatcher.of("..model");

                @Override
                public boolean apply(List<JavaClass> input) {

                  if (input.size() != 1) {
                    return false;
                  }

                  JavaClass javaClass = input.get(0);
                  return packageMatcher.matches(javaClass.getPackageName())
                      && javaClass.getName().endsWith("Dto");
                }
              })
          .forSubType();

  private static final DescribedPredicate<JavaMethod> HAS_DTO_RETURN_TYPE =
      rawReturnType(resideInAnyPackage("..model").and(simpleNameEndingWith("Dto")))
          .as("has DTO return type")
          .forSubType();

  private static final DescribedPredicate<JavaMethod> MAP_FROM_DTO_TO_ENTITY =
      HAS_DTO_PARAMETER_TYPE.and(HAS_ENTITY_RETURN_TYPE).as("map from a DTO to an entity");

  private static final DescribedPredicate<JavaMethod> MAP_FROM_ENTITY_TO_DTO =
      HAS_ENTITY_PARAMETER_TYPE.and(HAS_DTO_RETURN_TYPE).as("map from an entity to a DTO");

  private static final DescribedPredicate<JavaMethod> MAP_BETWEEN_AN_ENTITY_AND_A_DTO =
      MAP_FROM_DTO_TO_ENTITY.or(MAP_FROM_ENTITY_TO_DTO).as("map between an entity and a DTO");

  private static final DescribedPredicate<JavaMethod> ARE_DECLARED_IN_A_MAPPER_CLASS =
      declaredIn(annotatedWith(Mapper.class)).as("are declared in a mapper class").forSubType();

  @ArchTest
  static ArchRule layerAccess =
      layeredArchitecture()
          .layer("Controller")
          .definedBy("..web.api..")
          .layer("Service")
          .definedBy("..service..")
          .layer("Mapper")
          .definedBy("..mapper..")
          .layer("Persistence")
          .definedBy("..repository..")
          .whereLayer("Controller")
          .mayNotBeAccessedByAnyLayer()
          .whereLayer("Service")
          .mayOnlyBeAccessedByLayers("Mapper", "Controller")
          .whereLayer("Mapper")
          .mayOnlyBeAccessedByLayers("Controller")
          .whereLayer("Persistence")
          .mayOnlyBeAccessedByLayers("Service");

  @ArchTest
  static ArchRule entityManagerUse =
      classes()
          .that()
          .areAssignableTo(EntityManager.class)
          .should()
          .onlyBeAccessed()
          .byAnyPackage("..repository..");

  @ArchTest
  static ArchRule banNonSlf4JLogging =
      noClasses()
          .should()
          .accessClassesThat()
          .resideInAnyPackage("org.apache.log4j..", "org.apache.log4j2..", "java.util.logging..");

  @ArchTest
  static ArchRule banSpringUtils =
      noClasses().should().accessClassesThat().resideInAPackage("org.springframework.util..");

  @ArchTest
  static ArchRule banCommonsLang2 =
      noClasses().should().accessClassesThat().resideInAPackage("org.apache.commons.lang..");

  @ArchTest
  static ArchRule banShaded =
      noClasses()
          .should()
          .accessClassesThat()
          .resideInAnyPackage(
              "..shade..", "..shaded..", "..shadow..", "..shadowed..", "..external..")
          .orShould()
          .accessClassesThat()
          .haveNameMatching("[^.]+(?:\\.[^.]+)+\\.(com|org|net)\\..*");

  @ArchTest
  static ArchRule banNonCommonsLang3StringUtils = noClasses().should(USE_NON_LANG3_STRING_UTILS);

  @ArchTest static ArchRule banAccessToStandardStreams = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

  @ArchTest
  static ArchRule useStaticForArchTests =
      fields().that().areAnnotatedWith(ArchTest.class).should().beStatic();

  @ArchTest
  static ArchRule banArchTestsNoise =
      noFields()
          .that()
          .areAnnotatedWith(ArchTest.class)
          .should()
          .bePublic()
          .orShould()
          .bePrivate()
          .orShould()
          .beFinal();

  @ArchTest
  static ArchRule banJunit3 = noClasses().should().accessClassesThat().resideInAPackage("junit..");

  /** Ban JUnit 4 assertions in favour of AssertJ. */
  @ArchTest
  static ArchRule banJunit4Assertions =
      noClasses()
          .should(
              callMethodWhere(
                      JavaCall.Predicates.target(owner(type(Assert.class)))
                          .and(not(JavaCall.Predicates.target(name("fail")))))
                  .as("use JUnit4 assertions"));

  @ArchTest
  static ArchRule useContractAnnotationOnMapperMethods =
      methods()
          .that(ARE_DECLARED_IN_A_MAPPER_CLASS.and(MAP_BETWEEN_AN_ENTITY_AND_A_DTO))
          .should()
          .beAnnotatedWith(Contract.class);

  @ArchTest
  static ArchRule useNullableAnnotationOnMapperMethods =
      methods()
          .that(ARE_DECLARED_IN_A_MAPPER_CLASS.and(MAP_BETWEEN_AN_ENTITY_AND_A_DTO))
          .should()
          .beAnnotatedWith(Nullable.class);

  @ArchTest
  static ArchRule useNullableAnnotationOnMapperMethodParameter =
      methods()
          .that(ARE_DECLARED_IN_A_MAPPER_CLASS.and(MAP_BETWEEN_AN_ENTITY_AND_A_DTO))
          .should(
              new ArchCondition<JavaMethod>("have a single parameter annotated with @Nullable") {
                @Override
                public void check(JavaMethod archMethod, ConditionEvents events) {
                  Method method = archMethod.reflect();

                  boolean isNullable =
                      method.getParameters()[0].getAnnotation(Nullable.class) != null;

                  JavaClass archParameter = archMethod.getRawParameterTypes().get(0);
                  String message =
                      String.format(
                          "parameter %s %s annotated with @Nullable in %s",
                          archMethod.getFullName(),
                          isNullable ? "is" : "is not",
                          archMethod.getSourceCodeLocation());
                  events.add(new SimpleConditionEvent(archParameter, isNullable, message));
                }
              });

  @ArchTest
  static ArchRule banNonSpringNullable =
      noCodeUnits()
          .should()
          .beAnnotatedWith(
              new DescribedPredicate<JavaAnnotation<?>>("non-Spring @Nullable") {
                @Override
                public boolean apply(JavaAnnotation<?> annotation) {
                  return annotation.getRawType().getSimpleName().equals("Nullable")
                      && !annotation.getRawType().isAssignableTo(Nullable.class);
                }
              });

  @ArchTest
  static ArchRule banNonLombokNonNull =
      noCodeUnits()
          .should()
          .beAnnotatedWith(
              new DescribedPredicate<JavaAnnotation<?>>("non-Lombok @NonNull") {
                final List<String> nonNullAnnotationNames = Arrays.asList("NonNull", "NotNull");

                @Override
                public boolean apply(JavaAnnotation<?> annotation) {

                  return nonNullAnnotationNames.contains(annotation.getRawType().getSimpleName())
                      && !annotation.getRawType().isAssignableTo(NonNull.class);
                }
              });
}
