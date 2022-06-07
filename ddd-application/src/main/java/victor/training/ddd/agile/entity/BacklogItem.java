package victor.training.ddd.agile.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.EnumType.STRING;



/// JOIN inheritance strategy is the most inefficient from performance. Vlad Mihalcea and Thorben Janssen both say never to use it.

//abstract class BI {}
//class PI extends BI {}
//class SI extends BI {}
//
//new PI();
//

@Getter
@Setter
@NoArgsConstructor
@Entity // child entity of the  Sprint Aggregate
public class BacklogItem {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Product product;
    @Version // optimistic locking to detect cases when the same item is changed in parallel by 2 users.
    private Long version;
    // RULE: after being estimated, the ITEM cannot change anymore

    // ----- from here bellow is state of a SprintItem
    @NotNull
    private String title;
    private String description;
    // ------- until here is state of a BacklogItem (bound to a Product)

    @Enumerated(STRING)
    private Status status = Status.CREATED; // only makes sense while the item is IN A SPRINT
    @ManyToOne
    private Sprint sprint; // ⚠ not NULL when assigned to a sprint
    private Integer fpEstimation; // ⚠ not NULL when assigned to a sprint
    private int hoursConsumed; // only makes sense while the item is IN A SPRINT


    public void addHours(int hours) {
        hoursConsumed += hours;
    }

    void start() {
        if (status != BacklogItem.Status.CREATED) {
            throw new IllegalStateException("Item already started");
        }
        this.status = Status.STARTED;
    }

    void completeItem() {
        if (getStatus() != Status.STARTED) {
            throw new IllegalStateException("Cannot complete an Item before starting it");
        }
        setStatus(Status.COMPLETED);
    }

    public boolean isDone() {
        return getStatus() == Status.COMPLETED;
    }

    public enum Status {
        CREATED,
        STARTED,
        COMPLETED
    }
}
