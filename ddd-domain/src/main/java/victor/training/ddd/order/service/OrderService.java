package victor.training.ddd.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.ddd.order.model.Order;
import victor.training.ddd.order.model.OrderLine;
import victor.training.ddd.order.model.Product;
import victor.training.ddd.order.model.ProductSnapshot;
import victor.training.ddd.order.repo.OrderRepo;

import static java.util.Arrays.asList;

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



   public void main(String[] args) {
      // Logica central . eg placeOrder
      Product product = new Product(12);

      // place order
      ProductSnapshot snapshot = new ProductSnapshot(product.getId(), product.getPrice());
      OrderLine orderLine = new OrderLine(snapshot, 1);
      // intr-un @Service
      if (orderLine.isSuspect()) {
         System.out.println("Send email");
      }

      Order order = new Order(asList(orderLine));

      order.applyCoupon("123", .1);
   }
}
