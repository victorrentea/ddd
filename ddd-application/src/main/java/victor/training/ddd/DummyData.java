package victor.training.ddd;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import victor.training.ddd.model.Customer;
import victor.training.ddd.model.Customer.CustomerId;
import victor.training.ddd.model.CustomerAddress;
import victor.training.ddd.model.Site;
import victor.training.ddd.model.User;
import victor.training.ddd.repo.CustomerRepo;
import victor.training.ddd.repo.SiteRepo;
import victor.training.ddd.repo.UserRepo;

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
