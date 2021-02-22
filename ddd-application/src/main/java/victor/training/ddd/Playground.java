package victor.training.ddd;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.ddd.model.Customer;
import victor.training.ddd.model.Customer.CustomerId;
import victor.training.ddd.model.CustomerAddress;
import victor.training.ddd.repo.CustomerRepo;

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