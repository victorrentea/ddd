package victor.training.ddd.order.model;


import java.util.Objects;

public class OrderLine {
   private final Product product;
   private final int count;

   public OrderLine(Product product, int count) {
      if (count <= 0) {
         throw new IllegalArgumentException("Invalid count. Must be positive");
      }
      this.product = Objects.requireNonNull(product);
      this.count = count;
   }


   public Product getProduct() {
      return product;
   }

   public int getCount() {
      return count;
   }

   public int getPrice() {
      return count * product.getPrice();
   }
}
