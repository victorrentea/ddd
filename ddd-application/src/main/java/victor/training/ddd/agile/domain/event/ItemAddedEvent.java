package victor.training.ddd.agile.domain.event;

import lombok.Value;

@Value
public class ItemAddedEvent {
   String sprintBacklogItemId;
}