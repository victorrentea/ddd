package victor.training.ddd.agile.order.aggregates;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@Table(name = "ORDERS")
public class Order { // THE AGGREGATE ROOT or the "Order" aggregate
   @Id
   @GeneratedValue
   private Long id;

   private LocalDateTime createDate;

   @OneToMany
   @JoinColumn(name = "ORDER_ID")
   @Setter(AccessLevel.NONE)
   private List<OrderLine> orderLines = new ArrayList<>();

   private BigDecimal totalPrice; // business invariant; = sum of orderLines price

   void addLine(OrderLine orderLine) {
      orderLines.add(orderLine);
      totalPrice = totalPrice.add(orderLine.getPrice());
   }


   public List<OrderLine> getOrderLines() {
      return Collections.unmodifiableList(orderLines);
   }
}

class CodeUsingOrder {
   public void addLine(Order order, ProductToBuy product, int count, BigDecimal price) {
      OrderLine orderLine = new OrderLine();
      orderLine.setProductId(product.getId());
      orderLine.setItemPrice(price);
      orderLine.setItemCount(count);

      order.addLine(orderLine);
   }

   public void someoneNewToTheteam(Order order) {
      order.getOrderLines().add(new OrderLine());
   }

}
