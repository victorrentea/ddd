package victor.training.ddd;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.ddd.customer.model.Customer;
import victor.training.ddd.customer.model.Customer.CustomerId;
import victor.training.ddd.customer.model.CustomerAddress;
import victor.training.ddd.customer.repo.CustomerRepo;

import static java.util.Objects.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class Playground implements CommandLineRunner {
   private final Some some;
   @Override
   @Transactional
   public void run(String... args) throws Exception {
      log.info("I wanna play...");
      some.update();
      log.info("Done playing");
   }
}

@Service
@RequiredArgsConstructor
class Some {
   private final CustomerRepo customerRepo;

//   @Transactional
   public void update() {

//      EntityManager em;
//      em.getTransaction().begin();
      Customer aCustomer = customerRepo.findAll().get(0);
      long differentCountryId = 5L;
      extracted(aCustomer, differentCountryId);
      customerRepo.save(aCustomer);

      System.out.println(customerRepo.findById(new CustomerId(aCustomer.id().value())));
   }

   private void extracted(Customer aCustomer, long differentCountryId) {
      aCustomer.setAddress(new CustomerAddress(differentCountryId, "Paris", "Champs Elysees"));
   }
}


class Person {
   private FullName fullName;

   public FullName getFullName() {
      return fullName;
   }

   public void mary(Person him) {
      fullName = fullName.withLastName(him.fullName.getLastName());
   }
}

class FullName {
   private final String firstName;
   private final String lastName;

   FullName(String firstName, String lastName) {
      this.firstName = requireNonNull(firstName);
      this.lastName = requireNonNull(lastName);
   }

   public String getFirstName() {
      return firstName;
   }

   public String getLastName() {
      return lastName;
   }
   public FullName withLastName(String newLastName) {
       return new FullName(firstName, newLastName);
   }
}