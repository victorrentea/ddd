package victor.training.ddd.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.ddd.order.model.Customer;
import victor.training.ddd.order.model.events.OrderPlacedEvent;
import victor.training.ddd.order.repo.CustomerRepo;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerService {
   private final CustomerRepo customerRepo;

   @EventListener
   public void addFidelityPoints(OrderPlacedEvent event) {
      Customer customer = customerRepo.findById(event.getCustomerId()).get();
      customer.addFidelityPoints(event.getFidelityPoints());
      if (true) throw new RuntimeException("BuUU");
      customerRepo.save(customer);
   }
}
