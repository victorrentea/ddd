package victor.training.ddd.order.model;

import lombok.Value;

// Java 17 LTS
@Value
public class ProductSnapshot {
   String productId;
   double price;
}
