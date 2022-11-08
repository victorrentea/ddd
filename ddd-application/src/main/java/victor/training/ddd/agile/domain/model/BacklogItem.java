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


    //class SprintItem {
    // part of a new SprintItem that we'll extract from this entity.
    // that new @Entity will them "BELONG" to the Sprint Agg.
    @ManyToOne
    private Sprint sprint; // ⚠ not NULL when assigned to a sprint
    private Integer fpEstimation; // ⚠ not NULL when assigned to a sprint
    @Enumerated(STRING)
    private Status status = Status.CREATED;
    public enum Status {
        CREATED,
        STARTED,
        DONE
    }
    private int hoursConsumed;

    // HOW can we link Sprint Item to the BacklogItem it represents
    private Long backlogItemId; // +FK: opt1 + JPQL= SELECT bi.title, si.status FROM BacklogItem bi JOIN SprintItem si ON si.backlogItemId = bi.id

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
        if (this.status != Status.CREATED) {
            throw new IllegalStateException("Item already started");
        }
        this.status = Status.STARTED;
    }

    public void complete() {
        if (this.status != BacklogItem.Status.STARTED) {
            throw new IllegalStateException("Cannot complete an Item before starting it");
        }

        this.status = Status.DONE;
    }

    public Long getId() {
        return this.id;
    }

    public Product getProduct() {
        return this.product;
    }

    public @NotNull String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public Status getStatus() {
        return this.status;
    }

    public Integer getFpEstimation() {
        return this.fpEstimation;
    }

    public int getHoursConsumed() {
        return this.hoursConsumed;
    }

    public Long getVersion() {
        return this.version;
    }

    public BacklogItem setId(Long id) {
        this.id = id;
        return this;
    }

    public BacklogItem setProduct(Product product) {
        this.product = product;
        return this;
    }

    public BacklogItem setTitle(@NotNull String title) {
        this.title = title;
        return this;
    }

    public BacklogItem setDescription(String description) {
        this.description = description;
        return this;
    }

    BacklogItem setSprint(Sprint sprint) {
        this.sprint = sprint;
        return this;
    }

    public BacklogItem setFpEstimation(Integer fpEstimation) {
        this.fpEstimation = fpEstimation;
        return this;
    }

    public BacklogItem setHoursConsumed(int hoursConsumed) {
        this.hoursConsumed = hoursConsumed;
        return this;
    }

    public BacklogItem setVersion(Long version) {
        this.version = version;
        return this;
    }

}
