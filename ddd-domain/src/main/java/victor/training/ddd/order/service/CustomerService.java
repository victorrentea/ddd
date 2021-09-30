package victor.training.ddd.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.ddd.order.model.Customer;
import victor.training.ddd.order.repo.CustomerRepo;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerService {
   private final CustomerRepo customerRepo;

   public void addFidelityPoints(String customerId, int points) {
      Customer customer = customerRepo.findById(customerId).get();
      customer.addFidelityPoints(points);
      customerRepo.save(customer);
   }
}
