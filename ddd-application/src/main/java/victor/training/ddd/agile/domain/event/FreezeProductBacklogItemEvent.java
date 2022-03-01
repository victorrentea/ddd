package victor.training.ddd.agile.domain.event;

import lombok.Value;

@Value
public class FreezeProductBacklogItemEvent {
    long productBacklogItemId;
}
//@Value
//public class SprintBacklogItemCompletedEvent {
//   private final long sprintBacklogItemId;
//}

