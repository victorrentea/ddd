package victor.training.ddd.agile.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.EnumType.STRING;

// Child entity of the "Sprint" Aggregate.
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
    @Enumerated(STRING)
    private Status status = Status.CREATED;
    @ManyToOne
    private Sprint sprint; // ⚠ not NULL when assigned to a sprint
    private Integer fpEstimation; // ⚠ not NULL when assigned to a sprint
    private int hoursConsumed;
    @Version
    private Long version;

    public BacklogItem() {
    }

    public void complete() {
        if (status != Status.STARTED) {
          throw new IllegalStateException("Cannot complete an Item before starting it");
       }
        this.status = Status.DONE;
    }

     void start() {
        if (sprint.getStatus() != Sprint.Status.STARTED) {
            throw new IllegalStateException();
        }
        if (status != Status.CREATED) {
          throw new IllegalStateException("Item already started");
       }
        this.status = Status.STARTED;
    }

    public Long getId() {
        return id;
    }

    public BacklogItem setId(Long id) {
        this.id = id;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public BacklogItem setProduct(Product product) {
        this.product = product;
        return this;
    }

    public @NotNull String getTitle() {
        return title;
    }

    public BacklogItem setTitle(@NotNull String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public BacklogItem setDescription(String description) {
        this.description = description;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public BacklogItem setSprint(Sprint sprint) {
        this.sprint = sprint;
        return this;
    }

    public Integer getFpEstimation() {
        return fpEstimation;
    }

    public BacklogItem setFpEstimation(Integer fpEstimation) {
        this.fpEstimation = fpEstimation;
        return this;
    }

    public int getHoursConsumed() {
        return hoursConsumed;
    }

    public BacklogItem setHoursConsumed(int hoursConsumed) {
        this.hoursConsumed = hoursConsumed;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public BacklogItem setVersion(Long version) {
        this.version = version;
        return this;
    }

    public void addHours(int hours) {
        hoursConsumed += hours;
    }

    public enum Status {
        CREATED,
        STARTED,
        DONE
    }

}
