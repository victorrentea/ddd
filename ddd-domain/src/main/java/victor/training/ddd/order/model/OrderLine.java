package victor.training.ddd.order.model;


import java.util.Objects;

public class OrderLine {
   // FARA ID
//   id
   private ProductSnapshot productSnapshot;
//   private final String productId;
//   private final float productPrice;

   private final int count;
   private double discountRate = 0;

   public OrderLine(ProductSnapshot productSnapshot, int count) {
      if (count <= 0) {
         throw new IllegalArgumentException("Invalid count. Must be positive");
      }
      this.productSnapshot = Objects.requireNonNull(productSnapshot);
      this.count = count;
   }

   void setDiscountRate(double discountRate) {
      this.discountRate = discountRate;
   }

   public double getDiscountRate() {
      return discountRate;
   }

   public int getCount() {
      return count;
   }

   public ProductSnapshot getProductSnapshot() {
      return productSnapshot;
   }

   public double getPrice() {
      return count * productSnapshot.getPrice();
   }

   public boolean isSuspect() {
      return getPrice() > 100 && discountRate > .9;
   }


//   // MVC
//   public String asCsvLine() {
//      return product + ";" + count + "\n";
//   }

//   public OrderLineDto asDto() {
//      dto.count =count;
//   }
}
