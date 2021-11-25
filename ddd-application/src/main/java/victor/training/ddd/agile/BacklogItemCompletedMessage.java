package victor.training.ddd.agile;

import lombok.Value;
import victor.training.ddd.common.events.DomainEvent;

@Value
public class BacklogItemCompletedMessage implements DomainEvent {
   Long backlogItemId;
}
