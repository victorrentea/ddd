package victor.training.ddd.order.model;

//
public class Product {
   // imagine, shipped by, descr, carac MAP,
   private String id;
   private final int price;

   public String getId() {
      return id;
   }

   public Product(int price) {
      this.price = price;
   }

   public int getPrice() {
      return price;
   }

   public void reportPriceQueried() {
//      ++
   }
}
