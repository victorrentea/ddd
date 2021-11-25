package victor.training.ddd.agile;

import lombok.Value;
import victor.training.ddd.common.events.DomainEvent;

@Value
public class SprintFinishedEarlierEvent implements DomainEvent {
   SprintId sprintId;
}
