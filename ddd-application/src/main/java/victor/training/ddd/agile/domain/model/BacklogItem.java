package victor.training.ddd.agile.domain.model;

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


    public enum Status {
        CREATED,
        STARTED,
        DONE
    }

    @Enumerated(STRING)
    private Status status = Status.CREATED;

    @ManyToOne
    private Sprint sprint; // ⚠ not NULL when assigned to a sprint
    private Integer fpEstimation; // ⚠ not NULL when assigned to a sprint

    private int hoursConsumed;

    @Version
    private Long version;

    public void addHours(int hours) {
        hoursConsumed += hours;
    }


    public BacklogItem() {
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

    public Sprint getSprint() {
        return this.sprint;
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

    public BacklogItem setSprint(Sprint sprint) {
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
