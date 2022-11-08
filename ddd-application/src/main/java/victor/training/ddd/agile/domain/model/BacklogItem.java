package victor.training.ddd.agile.domain.model;

import victor.training.ddd.agile.domain.model.Sprint.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.EnumType.STRING;

@Entity
public class BacklogItem {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Product product;
    @NotNull
    private String title;
    private String description;


    private Integer fpEstimation; // ⚠ not NULL when assigned to a sprint
    @Enumerated(STRING)
    private Status status = Status.CREATED;
    public enum Status {
        CREATED,
        STARTED,
        DONE
    }
    private int hoursConsumed;

    // SHARED PK  opt2 JPQL= SELECT bi.title, si.status FROM BacklogItem bi JOIN SprintItem si ON si.id = bi.id
        // Pain FULL question to biz: can the same BacklogItem be assigned to 2 Sprint Items YES=> this is not an option
            // recycle entities? => loose historical data

    // private String title; // opt3: DATA DUPLICATE: risk = can the title CHANGE? .. after it was sterted? YES => keep in sync? how? Domain Events

    // opt4:  extends ? "Favor composition over inheritance"
//}


    @Version
    private Long version;

    public void addHours(int hours) {
        if (status != BacklogItem.Status.STARTED) {
            throw new IllegalStateException("Item not started");
        }
        hoursConsumed += hours;
    }


    protected BacklogItem() {
    }

    public BacklogItem(Product product, String title, String description) {
        this.product = product;
        this.title = title;
        this.description = description;
    }

    public void start() {
        if (status != Status.CREATED) {
            throw new IllegalStateException("Item already started");
        }
        this.status = Status.STARTED;
    }

    public void complete() {
        if (status != BacklogItem.Status.STARTED) {
            throw new IllegalStateException("Cannot complete an Item before starting it");
        }

        this.status = Status.DONE;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public @NotNull String getTitle() {
        return title;
    }

    public String getDescription() {
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

    public Long getVersion() {
        return version;
    }

    public BacklogItem setFpEstimation(Integer fpEstimation) {
        this.fpEstimation = fpEstimation;
        return this;
    }

}
