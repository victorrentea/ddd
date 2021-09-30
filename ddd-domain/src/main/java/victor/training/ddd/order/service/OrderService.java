package victor.training.ddd.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.ddd.order.model.Order;
import victor.training.ddd.order.repo.OrderRepo;

@Service
@RequiredArgsConstructor
public class OrderService {
   private final OrderRepo orderRepo;

   public void placeOrder(String orderId) {
      Order order = orderRepo.findById(orderId).get();
      order.place();
      orderRepo.save(order);
   }

}
