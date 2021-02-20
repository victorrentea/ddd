package victor.training.ddd.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Entity
public class OrderLine {
   @Id
   @GeneratedValue
   private Long id;

   private Long productId;
   private BigDecimal itemPrice;
   private int itemCount;

   protected OrderLine() {} // hibernate

   public OrderLine(Long productId, BigDecimal itemPrice, int itemCount) {
      this.productId = requireNonNull(productId);
      this.itemPrice = requireNonNull(itemPrice);
      this.itemCount = itemCount;
   }

   public BigDecimal computePrice() {
      return itemPrice.multiply(BigDecimal.valueOf(itemCount));
   }
}
