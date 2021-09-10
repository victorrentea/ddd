package victor.training.ddd.order.model;

public class Product {
   private final int price;

   public Product(int price) {
      this.price = price;
   }

   public int getPrice() {
      return price;
   }
}
