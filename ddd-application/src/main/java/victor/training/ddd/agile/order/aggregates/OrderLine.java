package victor.training.ddd.agile.order.aggregates;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Data
@Entity
public class OrderLine { // child entity of the Order Agregate Root
   @Id
   @GeneratedValue
   private Long id;

//   @ManyToOne
//   private ProductToBuy product;
   private Long productId; // + don;'t drop the FK (yet)

   private int itemCount;

   private BigDecimal itemPrice;

   public BigDecimal getPrice() {
      return getItemPrice().multiply(BigDecimal.valueOf(getItemCount()));
   }
}
