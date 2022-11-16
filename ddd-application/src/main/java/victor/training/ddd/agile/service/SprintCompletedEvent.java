package victor.training.ddd.agile.service;

import lombok.Value;
import victor.training.ddd.agile.entity.Sprint;

@Value
public class SprintCompletedEvent {
  long sprintId; // thin Events = "notification"
}
