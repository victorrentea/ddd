package victor.training.ddd.agile.events;

import lombok.Value;

@Value
public class BacklogItemAddedToSprintEvent {
    Long backlogId;
}
