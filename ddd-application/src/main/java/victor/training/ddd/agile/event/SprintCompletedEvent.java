package victor.training.ddd.agile.event;

public class SprintCompletedEvent {
  private final long sprintId;

  public SprintCompletedEvent(long sprintId) {
    this.sprintId = sprintId;
  }

  public long getSprintId() {
    return sprintId;
  }
}
