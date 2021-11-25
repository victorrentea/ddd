package victor.training.ddd.agile;

import lombok.Value;
import victor.training.ddd.common.events.DomainEvent;

import java.util.List;

@Value
public class BlackSprintEvent implements DomainEvent {
   String productCode;
   List<Long> notDoneItemIds;
}
