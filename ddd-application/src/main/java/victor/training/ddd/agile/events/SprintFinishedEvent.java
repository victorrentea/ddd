package victor.training.ddd.agile.events;

import lombok.Value;

@Value
public class SprintFinishedEvent {
    long sprintId;
}
