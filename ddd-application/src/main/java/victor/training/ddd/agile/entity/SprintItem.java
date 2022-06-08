package victor.training.ddd.agile.entity;

import org.springframework.util.Assert;

import javax.persistence.*;

import java.util.Objects;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;



/// JOIN inheritance strategy is the most inefficient from performance. Vlad Mihalcea and Thorben Janssen both say never to use it.

//abstract class BI {}
//class PI extends BI {}
//class SI extends BI {}
//
//new PI();
//

@Entity // child entity of the  Sprint Aggregate
public class SprintItem {

    public enum Status {
        CREATED,
        STARTED,
        COMPLETED
    }
    @Id
    private String id = UUID.randomUUID().toString();
    private String title;
    private String description;

    @Enumerated(STRING)
    private Status status = Status.CREATED;
    private Integer fpEstimation;
    private int hoursConsumed;


    protected SprintItem() { // for Hibernate only
    }
    public SprintItem(String title, String description, int fpEstimation) {
        this.title = Objects.requireNonNull(title);
        this.description = Objects.requireNonNull(description);
        Assert.isTrue(fpEstimation > 0, "estimation is non zero");
        this.fpEstimation = fpEstimation;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() { // normally it should have been used in a prod syst
        return description;
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

    public void logHours(int hours) {
        if (status != Status.STARTED) {
            throw new IllegalStateException("Item not started");
        }
        hoursConsumed += hours;
    }

    void start() {
        if (status != SprintItem.Status.CREATED) {
            throw new IllegalStateException("Item already started");
        }
        this.status = Status.STARTED;
    }

    void completeItem() {
        if (getStatus() != Status.STARTED) {
            throw new IllegalStateException("Cannot complete an Item before starting it");
        }
        status = Status.COMPLETED;
    }

    public boolean isDone() {
        return getStatus() == Status.COMPLETED;
    }


}
