package victor.training.ddd.order.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import victor.training.ddd.common.events.DomainEventsPublisher;
import victor.training.ddd.order.model.events.OrderPlacedEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;

@Getter
@Document
public class Order {
   enum Status {
      DRAFT,
      PLACED,
      SHIPPED
   }

   @Id
   private ObjectId id;
   private final List<OrderLine> orderLines = new ArrayList<>();
   private double totalPrice;
   @Setter
   private CustomerId customerId;

   private Status status = Status.DRAFT;

   private final LocalDateTime createTime = LocalDateTime.now();
   private LocalDateTime dateShipped;

   private String shippedByUser; // never null if status >= SHIPPED

   public Order(List<OrderLine> orderLines) {
      if (orderLines.isEmpty()) {
         throw new IllegalArgumentException("At least one OrderLine is required");
      }
      for (OrderLine orderLine : orderLines) {
         addProduct(orderLine.getProduct().getProductId(),
             orderLine.getProduct().getPrice(),
             orderLine.getCount());
      }
   }

   public void ship(String username) {
      if (status != Status.PLACED) {
         throw new IllegalStateException("Should have been placed");
      }
      status = Status.SHIPPED;
      shippedByUser = Objects.requireNonNull(username);
      dateShipped = LocalDateTime.now();
   }


   public void place() {
      if (status != Status.DRAFT) {
         throw new IllegalStateException("Should have been draft");
      }
      status = Status.PLACED;
      // cum puii mei o fac pe asta sa mearga ?!
      DomainEventsPublisher.publish(new OrderPlacedEvent(customerId.getId(), getFidelityPoints()));
   }

   public List<OrderLine> getOrderLines() {
      return unmodifiableList(orderLines);
   }

   public void addProduct(String productId, double price, int items) {
      Optional<OrderLine> orderLineOpt = orderLines.stream()
          .filter(line -> line.getProduct().getProductId().equals(productId))
          .findFirst();

      if (orderLineOpt.isPresent()) {
         OrderLine existingLine = orderLineOpt.get();
         orderLines.remove(existingLine);
         orderLines.add(existingLine.addItems(items));
      } else {
         orderLines.add(new OrderLine(new ProductSnapshot(productId, price), items));
      }
      updateTotalPrice();
   }

   private void updateTotalPrice() {
      totalPrice = orderLines.stream().mapToDouble(OrderLine::getPrice).sum();
   }

   public void removeProduct(OrderLine orderLine) {
      // invariant: sum(orderLine.product.price * orderLine.count)=order.totalPrice
      if (orderLines.size() == 1) {
         throw new IllegalArgumentException("The order must have at least one OrderLine");
      }
      orderLines.remove(orderLine);
      updateTotalPrice();
   }

   public int getFidelityPoints() {
      return (int) (totalPrice / 10);
   }
}

