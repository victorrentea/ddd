package victor.training.ddd.service.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.ddd.model.Order;
import victor.training.ddd.repo.OrderRepo;
import victor.training.ddd.service.events.CustomerCreatedEvent;

import java.util.List;

import static org.springframework.data.jpa.domain.Specification.*;
import static victor.training.ddd.repo.OrderSpec.payed;
import static victor.training.ddd.repo.OrderSpec.shipped;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

   @EventListener
   public void onCustomerCreatedEvent(CustomerCreatedEvent event) {
      System.out.println("DO stuff");
   }

}
