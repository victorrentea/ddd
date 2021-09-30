package victor.training.ddd.order.model;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;


// Aggregate si Entitate
//@Entity
//@Data
public class Order {
   private String id;
   private List<OrderLine> orderLines = new ArrayList<>();
   private double totalPrice;
   private LocalDateTime dateShipped;
   private LocalDateTime createTime;
   private CustomerId customerId;


   public Double getTotalPrice() {
      return totalPrice;
   }

   public Order(List<OrderLine> orderLines) {
      if (orderLines.isEmpty()) {
         throw new IllegalArgumentException("At least one OrderLine is required");
      }
      for (OrderLine orderLine : orderLines) {
         addProduct(orderLine.getProductSnapshot().getProductId(),
             orderLine.getProductSnapshot().getPrice(),
             orderLine.getCount());
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

   public void addProduct(String productId, double price, int items) {
      Optional<OrderLine> orderLineOpt = orderLines.stream()
          .filter(line -> line.getProductSnapshot().getProductId().equals(productId))
          .findFirst();

      if (orderLineOpt.isPresent()) {
         OrderLine existingLine = orderLineOpt.get();
         orderLines.remove(existingLine);
         OrderLine newOrderLine = existingLine.addItems(items);
         orderLines.add(newOrderLine);
      } else {
         orderLines.add(new OrderLine(new ProductSnapshot(productId, price), items));
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

      orderLines.remove(orderLine);
      orderLines.add(orderLine.withDiscountRate(discountRate));
      updateTotalPrice();
//      publishEvent(new PriceRequestedEvent(productId));
   }
}

