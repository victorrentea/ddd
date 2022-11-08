package victor.training.ddd.agile.domain.events;

public class SprintCompletedEvent {
    private final Long sprintId;

    public SprintCompletedEvent(Long sprintId) {
        this.sprintId = sprintId;
    }

    public Long getSprintId() {
        return sprintId;
    }
}
