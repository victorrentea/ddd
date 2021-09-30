package victor.training.ddd.order.model.events;

import lombok.Value;
import victor.training.ddd.common.events.DomainEvent;

@Value
public class OrderPlacedEvent implements DomainEvent {
   String customerId;
   int fidelityPoints;
}
