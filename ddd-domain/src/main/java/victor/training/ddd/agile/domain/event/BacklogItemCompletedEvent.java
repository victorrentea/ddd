package victor.training.ddd.agile.domain.event;

import lombok.Value;
import victor.training.ddd.common.events.DomainEvent;

@Value
public class BacklogItemCompletedEvent implements DomainEvent {
   Long backlogItemId;
}
