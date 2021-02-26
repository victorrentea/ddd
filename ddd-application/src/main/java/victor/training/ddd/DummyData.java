package victor.training.ddd;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.ddd.customer.model.Customer;
import victor.training.ddd.customer.model.Customer.CustomerId;
import victor.training.ddd.customer.model.CustomerAddress;
import victor.training.ddd.customer.model.Site;
import victor.training.ddd.user.model.User;
import victor.training.ddd.customer.repo.CustomerRepo;
import victor.training.ddd.customer.repo.SiteRepo;
import victor.training.ddd.user.repo.UserRepo;

import javax.annotation.PostConstruct;
import java.util.stream.Stream;

//@Profile("insertDummyData") // in real project
@Component
@RequiredArgsConstructor
public class DummyData {
   private final UserRepo userRepo;
   private final SiteRepo siteRepo;
   private final CustomerRepo customerRepo;

   @PostConstruct
   public void insertDummyData() {
      Stream.of("eric", "vaughn", "greg", "scott").map(User::new).forEach(userRepo::save);

      Long siteId = siteRepo.save(new Site("Under the sea")).id();
      CustomerAddress address = new CustomerAddress(1L, "Paris", "Champs Elysees");
      CustomerId id = customerRepo.newId();
      Customer customer = new Customer(id, "John", address,"a@b.com", siteId);

      customerRepo.save(customer);

   }
}
