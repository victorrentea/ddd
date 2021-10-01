package victor.training.ddd.order.model;

import org.springframework.context.event.EventListener;
import victor.training.ddd.order.model.events.OrderPlacedEvent;

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
