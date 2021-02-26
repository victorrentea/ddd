package victor.training.ddd.service.events;

import org.springframework.context.ApplicationEvent;

public class CustomerCreatedEvent {
   private final int id;

   public CustomerCreatedEvent(int id) {
      this.id = id;
   }

   public int getId() {
      return id;
   }
}
