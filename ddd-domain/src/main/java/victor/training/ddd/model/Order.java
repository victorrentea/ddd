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

   private BigDecimal totalPrice = BigDecimal.ZERO;

   @OneToMany
   private List<OrderLine> orderLines;

   // TODO 1 factory method for OrderLines
   // TODO 2 hide OrderLine completely
   public void add(OrderLine orderLine) {
      orderLines.add(orderLine);
      totalPrice = totalPrice.add(orderLine.computePrice());
   }

   // hiding OrderLine child entity
   public void setItemCount(Long productId, int newCount) {
      OrderLine orderLine = orderLines.stream().filter(line -> line.productId().equals(productId)).findFirst().get();
      BigDecimal previousLinePrice = orderLine.computePrice();
      orderLine.itemCount(newCount);
      BigDecimal newLinePrice = orderLine.computePrice();
      totalPrice = totalPrice.add(newLinePrice).subtract(previousLinePrice);
   }
}
