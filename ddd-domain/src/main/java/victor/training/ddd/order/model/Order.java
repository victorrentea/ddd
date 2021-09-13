package victor.training.ddd.order.model;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;

// Aggregate si Entitate
//@Entity
//@Configurable
//@Data
public class Order {

//   @Autowired
//   private OrderRepo repo;

//   private ImmutableList<OrderLine> orderLines = new ArrayList<>();
//   @Size(min = 1)
   private String id;
   private List<OrderLine> orderLines = new ArrayList<>();
   private double totalPrice;
   private LocalDateTime dateShipped;
   private LocalDateTime createTime;


   public Double getTotalPrice() {
      return totalPrice;
   }

   public Order(List<OrderLine> orderLines) {
      if (orderLines.isEmpty()) {
         throw new IllegalArgumentException("At least one OrderLine is required");
      }
      for (OrderLine orderLine : orderLines) {
         addProduct(orderLine);
      }
   }

   public String getId() {
      return "a";
   }

   @Transactional
   public void setDateShipped(LocalDateTime dateShipped) {
      if (dateShipped.isBefore(LocalDateTime.now().minusMonths(1))) {
         throw new IllegalArgumentException("Data e prea in trecut");
      }
      this.dateShipped = dateShipped;
   }

   public List<OrderLine> getOrderLines() {
      return unmodifiableList(orderLines);
   }

   public void addProduct(OrderLine orderLine) {
      // invariant: sum(orderLine.product.price * orderLine.count)=order.totalPrice
      Optional<OrderLine> orderLineOpt = orderLines.stream()
          .filter(line -> line.getProductSnapshot() == orderLine.getProductSnapshot()).findFirst();

      if (orderLineOpt.isPresent()) {
         orderLines.remove(orderLineOpt.get());
         int oldCount = orderLineOpt.get().getCount();
         int newCount = oldCount + orderLine.getCount();
         orderLines.add(new OrderLine(orderLine.getProductSnapshot(), newCount));
      } else {
         orderLines.add(orderLine);
      }
      updateTotalPrice();
   }

   private void updateTotalPrice() {
      totalPrice += orderLines.stream().mapToDouble(OrderLine::getPrice).sum();
   }

   public void removeProduct(OrderLine orderLine) {
      // invariant: sum(orderLine.product.price * orderLine.count)=order.totalPrice
      if (orderLines.size() == 1) {
         throw new IllegalArgumentException("The order must have at least one OrderLine");
      }
      orderLines.remove(orderLine);
      updateTotalPrice();
   }

   public void applyCoupon(String productId, double discountRate) {
      if (discountRate > 1) {
         throw new IllegalArgumentException();
      }
      // aplica discount pe orderlineUL cu produsul asta
      OrderLine orderLine = orderLines.stream()
          .filter(line -> line.getProductSnapshot().getProductId().equals(productId)).findFirst()
          .get();
      orderLine.setDiscountRate(discountRate);
      updateTotalPrice();
//      publishEvent(new PriceRequestedEvent(productId));
   }
}

class OrderFactory {

}

