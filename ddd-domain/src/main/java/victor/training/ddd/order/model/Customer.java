package victor.training.ddd.order.model;

// Aggregate

@interface DDD {
   @interface Aggregate {}
   @interface ValueObject {}
}

@DDD.Aggregate
public class Customer {
   private String id;
   private int fidelityPoints;

   public void addFidelityPoints(int addedPoints) {
      this.fidelityPoints += addedPoints;
   }

   public int getFidelityPoints() {
      return fidelityPoints;
   }
}
