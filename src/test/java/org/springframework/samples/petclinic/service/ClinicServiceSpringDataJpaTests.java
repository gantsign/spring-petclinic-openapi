package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test of the Service and the Repository layer.
 *
 * <p>ClinicServiceSpringDataJpaTests subclasses benefit from the following services provided by the
 * Spring TestContext Framework:
 *
 * <ul>
 *   <li><strong>Spring IoC container caching</strong> which spares us unnecessary set up time
 *       between test execution.
 *   <li><strong>Dependency Injection</strong> of test fixture instances, meaning that we don't need
 *       to perform application context lookups. See the use of {@link Autowired @Autowired} on the
 *       <code>{@link
 * ClinicServiceSpringDataJpaTests#clinicService clinicService}</code> instance variable, which uses
 *       autowiring <em>by type</em>.
 *   <li><strong>Transaction management</strong>, meaning each test method is executed in its own
 *       transaction, which is automatically rolled back by default. Thus, even if tests insert or
 *       otherwise change database state, there is no need for a teardown or cleanup script.
 *   <li>An {@link org.springframework.context.ApplicationContext ApplicationContext} is also
 *       inherited and can be used for explicit bean lookup if necessary.
 * </ul>
 *
 * @author Ken Krebs
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ClinicServiceSpringDataJpaTests {

  @Autowired protected ClinicService clinicService;

  @Test
  public void shouldFindOwnersByLastName() {
    Collection<Owner> owners = clinicService.findOwnerByLastName("Davis");
    assertThat(owners.size()).isEqualTo(2);

    owners = clinicService.findOwnerByLastName("Daviss");
    assertThat(owners.isEmpty()).isTrue();
  }

  @Test
  public void shouldFindSingleOwnerWithPet() {
    Owner owner = clinicService.findOwnerById(1);
    assertThat(owner.getLastName()).startsWith("Franklin");
    assertThat(owner.getPets().size()).isEqualTo(1);
    assertThat(owner.getPets().iterator().next().getType()).isNotNull();
    assertThat(owner.getPets().iterator().next().getType().getName()).isEqualTo("cat");
  }

  @Test
  @Transactional
  public void shouldInsertOwner() {
    Collection<Owner> owners = clinicService.findOwnerByLastName("Schultz");
    int found = owners.size();

    Owner owner = new Owner();
    owner.setFirstName("Sam");
    owner.setLastName("Schultz");
    owner.setAddress("4, Evans Street");
    owner.setCity("Wollongong");
    owner.setTelephone("4444444444");
    clinicService.saveOwner(owner);
    assertThat(owner.getId().longValue()).isNotEqualTo(0);

    owners = clinicService.findOwnerByLastName("Schultz");
    assertThat(owners.size()).isEqualTo(found + 1);
  }

  @Test
  @Transactional
  public void shouldUpdateOwner() {
    Owner owner = clinicService.findOwnerById(1);
    String oldLastName = owner.getLastName();
    String newLastName = oldLastName + 'X';

    owner.setLastName(newLastName);
    clinicService.saveOwner(owner);

    // retrieving new name from database
    owner = clinicService.findOwnerById(1);
    assertThat(owner.getLastName()).isEqualTo(newLastName);
  }

  @Test
  public void shouldFindPetWithCorrectIdAndOwnerId() {
    Pet pet7 = clinicService.findPetByIdAndOwnerId(7, 6);
    assertThat(pet7.getName()).startsWith("Samantha");
    assertThat(pet7.getOwner().getFirstName()).isEqualTo("Jean");
  }

  @Test
  public void shouldFindAllPetTypes() {
    Collection<PetType> petTypes = clinicService.findPetTypes();

    PetType petType1 = EntityUtils.getById(petTypes, PetType.class, 1);
    assertThat(petType1.getName()).isEqualTo("cat");
    PetType petType4 = EntityUtils.getById(petTypes, PetType.class, 4);
    assertThat(petType4.getName()).isEqualTo("snake");
  }

  @Test
  @Transactional
  public void shouldInsertPetIntoDatabaseAndGenerateId() {
    Owner owner6 = clinicService.findOwnerById(6);
    int found = owner6.getPets().size();

    Pet pet = new Pet();
    pet.setName("bowser");
    Collection<PetType> types = clinicService.findPetTypes();
    pet.setType(EntityUtils.getById(types, PetType.class, 2));
    pet.setBirthDate(LocalDate.of(2019, 1, 2));
    owner6.addPet(pet);
    assertThat(owner6.getPets().size()).isEqualTo(found + 1);

    clinicService.savePet(pet);
    clinicService.saveOwner(owner6);

    owner6 = clinicService.findOwnerById(6);
    assertThat(owner6.getPets().size()).isEqualTo(found + 1);
    // checks that ID has been generated
    assertThat(pet.getId()).isNotNull();
  }

  @Test
  @Transactional
  public void shouldUpdatePetName() throws Exception {
    Pet pet7 = clinicService.findPetByIdAndOwnerId(7, 6);
    String oldName = pet7.getName();

    String newName = oldName + 'X';
    pet7.setName(newName);
    clinicService.savePet(pet7);

    pet7 = clinicService.findPetByIdAndOwnerId(7, 6);
    assertThat(pet7.getName()).isEqualTo(newName);
  }

  @Test
  public void shouldFindVets() {
    Collection<Vet> vets = clinicService.findVets();

    Vet vet = EntityUtils.getById(vets, Vet.class, 3);
    assertThat(vet.getLastName()).isEqualTo("Douglas");
    assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
    assertThat(vet.getSpecialties().stream().map(Specialty::getName))
        .containsExactlyInAnyOrder("dentistry", "surgery");
  }

  @Test
  @Transactional
  public void shouldAddNewVisitForPet() {
    Pet pet7 = clinicService.findPetByIdAndOwnerId(7, 6);
    int found = pet7.getVisits().size();
    Visit visit = new Visit();
    pet7.addVisit(visit);
    visit.setDescription("test");
    clinicService.saveVisit(visit);
    clinicService.savePet(pet7);

    pet7 = clinicService.findPetByIdAndOwnerId(7, 6);
    assertThat(pet7.getVisits().size()).isEqualTo(found + 1);
    assertThat(visit.getId()).isNotNull();
  }

  @Test
  public void shouldFindVisitsByPetId() throws Exception {
    Collection<Visit> visits = clinicService.findVisitsByPetId(7);
    assertThat(visits.size()).isEqualTo(2);
    Visit[] visitArr = visits.toArray(new Visit[0]);
    assertThat(visitArr[0].getPet()).isNotNull();
    assertThat(visitArr[0].getDate()).isNotNull();
    assertThat(visitArr[0].getPet().getId()).isEqualTo(7);
  }
}
