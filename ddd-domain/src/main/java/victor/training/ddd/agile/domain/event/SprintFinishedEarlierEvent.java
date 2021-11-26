package victor.training.ddd.agile.domain.event;

import lombok.Value;
import victor.training.ddd.agile.domain.entity.SprintId;
import victor.training.ddd.common.events.DomainEvent;

@Value
public class SprintFinishedEarlierEvent implements DomainEvent {
   SprintId sprintId;
}
