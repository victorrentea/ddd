package victor.training.ddd.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Order {
   @Id
   @GeneratedValue
   private Long id;

   private BigDecimal totalPrice;

   @OneToMany
   private List<OrderLine> orderLines;

   public void add(OrderLine orderLine) {
      orderLines.add(orderLine);
      totalPrice = totalPrice.add(orderLine.computePrice());
   }
}
