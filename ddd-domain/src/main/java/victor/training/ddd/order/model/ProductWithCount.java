package victor.training.ddd.order.model;

import lombok.Data;
import victor.training.ddd.product.model.Product;

@Data
public class ProductWithCount {
   private final Product product;
   private final int count;
}
