package victor.training.ddd.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import victor.training.ddd.order.model.Order;
import victor.training.ddd.order.repo.OrderRepo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
   private final OrderRepo orderRepo;
   private final ApplicationEventPublisher publisher;
   private final OrderEventsSender orderEventsSender;

   public void suppliersOrdersData(Order order) {


      Optional<Order> byId = orderRepo.findById("1");


      // TODO : make sure order is COMMIT in your DB before you send the event
//      sendOrdersToSuppliers.suppliersOrdersData(order);
//      publisher.publishEvent(new OrderConfirmedEvent(order.id()));

//      orderEventsSender.sendOrderConfirmed(order.id());
   }

   public List<Order> search(OrderSearchCriteria criteria) {
      List<Order> results = Collections.emptyList();
      return results;
   }



}
