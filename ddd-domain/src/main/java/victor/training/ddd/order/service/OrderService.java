package victor.training.ddd.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.ddd.order.model.Customer;
import victor.training.ddd.order.model.Order;
import victor.training.ddd.order.repo.CustomerRepo;
import victor.training.ddd.order.repo.OrderRepo;

@Service
@RequiredArgsConstructor
public class OrderService {
   private final OrderRepo orderRepo;

   @Transactional
   public void applyCoupon(String orderId, String productId) {
      Order order = orderRepo.findById(orderId)
          .orElseThrow(() ->  new IllegalArgumentException("Order ID not found"));
      order.applyCoupon(productId, 0.1);
      orderRepo.save(order);
   }

   private final CustomerRepo customerRepo;

   public void placeOrder(String orderId) {
      Order order = orderRepo.findById(orderId).get();
      order.place();

      Customer customer = customerRepo.findById(order.getCustomerId().getId()).get();
      customer.addFidelityPoints(order.getFidelityPoints());
   }

}
