package victor.training.ddd.agile.domain.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.EnumType.STRING;

@Entity
public class SprintItem {
    @Id
    private Long id;

    private Long backlogItemId;
    private Integer fpEstimation; // ⚠ not NULL when assigned to a sprint
    @Enumerated(STRING)
    private Status status = Status.CREATED;
    public enum Status {
        CREATED,
        STARTED,
        DONE
    }
    private int hoursConsumed;

    public void addHours(int hours) {
        if (status != SprintItem.Status.STARTED) {
            throw new IllegalStateException("Item not started");
        }
        hoursConsumed += hours;
    }


    protected SprintItem() {
    }

    public SprintItem(Long id, Long backlogItemId, Integer fpEstimation) {
        this.id = id;
        this.backlogItemId = backlogItemId;
        this.fpEstimation = fpEstimation;
    }


    public void start() {
        if (status != Status.CREATED) {
            throw new IllegalStateException("Item already started");
        }
        this.status = Status.STARTED;
    }

    public void complete() {
        if (status != SprintItem.Status.STARTED) {
            throw new IllegalStateException("Cannot complete an Item before starting it");
        }

        this.status = Status.DONE;
    }

    public Long getBacklogItemId() {
        return backlogItemId;
    }

    public Long getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public Integer getFpEstimation() {
        return fpEstimation;
    }

    public int getHoursConsumed() {
        return hoursConsumed;
    }


}
