package victor.training.ddd.order.model;

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
