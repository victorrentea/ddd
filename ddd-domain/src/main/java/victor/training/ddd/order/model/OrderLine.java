package victor.training.ddd.order.model;


import lombok.Getter;

import static java.util.Objects.requireNonNull;

@Getter
public class OrderLine {
   private final ProductSnapshot product;
   private final int count;

   public OrderLine(ProductSnapshot product, int count) {
      this.product = requireNonNull(product);
      if (count <= 0) {
         throw new IllegalArgumentException("Invalid count. Must be positive");
      }
      this.count = count;
   }

   public double getPrice() {
      return count * product.getPrice();
   }

   public OrderLine addItems(int newItems) {
      return new OrderLine(product, count + newItems);
   }

}
