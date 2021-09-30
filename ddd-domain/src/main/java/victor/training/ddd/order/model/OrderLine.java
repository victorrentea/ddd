package victor.training.ddd.order.model;


import java.util.Objects;

// Aggregate
public class OrderLine {

   private final ProductSnapshot productSnapshot;
   private final int count;
   private final double discountRate;

   public OrderLine(ProductSnapshot productSnapshot, int count, double discountRate) {
      if (count <= 0) {
         throw new IllegalArgumentException("Invalid count. Must be positive");
      }
      this.productSnapshot = Objects.requireNonNull(productSnapshot);
      this.count = count;
      this.discountRate = discountRate;
   }
   public OrderLine(ProductSnapshot productSnapshot, int count) {
      this(productSnapshot, count, 0);
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

   public OrderLine addItems(int newItems) {
      return new OrderLine(productSnapshot, count + newItems, discountRate);
   }

   public OrderLine withDiscountRate(double discountRate) {
      return new OrderLine(productSnapshot, count, discountRate);
   }


//   // MVC
//   public String asCsvLine() {
//      return product + ";" + count + "\n";
//   }

//   public OrderLineDto asDto() {
//      dto.count =count;
//   }
}
