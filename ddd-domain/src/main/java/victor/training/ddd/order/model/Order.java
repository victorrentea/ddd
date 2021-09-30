package victor.training.ddd.order.model;

import lombok.Getter;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;

class Cod {
   {
//      Order order;
//
//      try {
//         order.ship(null);
//      } catch (Exception e) {
//         // shaworma
//      }
//      repo.save(order);

//      export toate orderurile cu user.FullName shipped by

      // count(User) < 10k : poti sa-i incarci pe toti in memorie
      // Map<String, String> usernameToFullname

      // daca count(User) >>> mare  iterezi pe order si tii acelasi map
      // dar pe care il populezi pe parcurs
      // alternativa : @Cacheable
   }
}

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
   @Getter
   private Status status;
   @Getter
   private String shippedByUser; // never null if status >= SHIPPED

   public CustomerId getCustomerId() {
      return customerId;
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

   public void ship(String username) {
      if (status != Status.PLACED) {
         throw new IllegalStateException("Should have been placed");
      }
      status = Status.SHIPPED;
      shippedByUser = Objects.requireNonNull(username);
   }

   public void place(Customer customer) {
      if (status != Status.DRAFT) {
         throw new IllegalStateException("Should have been draft");
      }
      status = Status.PLACED;
      customer.addFidelityPoints(getFidelityPoints());
   }


   public Double getTotalPrice() {
      return totalPrice;
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

   public int getFidelityPoints() {
      return (int) (totalPrice / 10);
   }

   enum Status {
      DRAFT,
      PLACED,
      SHIPPED
   }
}

