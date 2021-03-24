package victor.training.ddd.order.model;

import lombok.Data;
import victor.training.ddd.product.model.Product;

import java.math.BigDecimal;

@Data
public class ProductWithQuantity {
   private final Product product;
   private final BigDecimal quantity;

   public ProductWithQuantity(Product product, BigDecimal count) {
      this.product = product;
      this.quantity = count;
   }

   public Product product() {
      return product;
   }

   public BigDecimal quantity() {
      return quantity;
   }
}
