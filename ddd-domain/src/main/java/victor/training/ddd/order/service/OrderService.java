package victor.training.ddd.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.ddd.order.model.Order;
import victor.training.ddd.order.repo.OrderRepo;

@Service
@RequiredArgsConstructor
public class OrderService {
   private final OrderRepo orderRepo;
   private final CustomerService customerService;

   @Transactional
   public void applyCoupon(String orderId, String productId) {
      Order order = orderRepo.findById(orderId)
          .orElseThrow(() ->  new IllegalArgumentException("Order ID not found"));
      order.applyCoupon(productId, 0.1);
      orderRepo.save(order);
   }

   public void placeOrder(String orderId) {
      Order order = orderRepo.findById(orderId).get();
      order.place();
      orderRepo.save(order);

      customerService.addFidelityPoints(order.getCustomerId().getId(), order.getFidelityPoints());
   }

}
