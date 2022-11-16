package victor.training.ddd.agile.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@Entity
public class BacklogItem {
  @Id
  @GeneratedValue
  private Long id;
  @ManyToOne
  private Product product; // NO TODO replace with numeric productId Long

  @NotNull
  private String title;
  private String description;

  public enum Status {
    CREATED,
    STARTED,
    DONE
  }
  @Version
  private Long version;

  // TODO est 4h : move the fields below into a SprintItem class, that will have to also keep a backlogId FK to the original item
  // TODO CR: (edit the test) we have to be able to add the same item in two sprints!
  // the fields below this line only make sense after the item becomes part of a Sprint. -------------
  @Enumerated(STRING)
  private Status status = Status.CREATED;

  // TODO remove the bidirectional link
  @ManyToOne
  private Sprint sprint; // ⚠ not NULL after it's assigned to a sprint < dissapear bidirectional dep -> KILL
  private Integer fpEstimation; // ⚠ not NULL when assigned to a sprint <- required field to the Sprint Item
    // the smaller domain objects you have, the more constraints you can implement in them
//  ==> domain breakdown => more words "Sprint Item" in the discussion with Biz, spec, req, .feature

  private int hoursConsumed;



  public void addHours(int hours) {
    hoursConsumed += hours;
  }


  public BacklogItem() {
  }

  public void complete() {
    if (getStatus() != Status.STARTED) {
      throw new IllegalStateException("Cannot complete an Item before starting it");
    }
    setStatus(Status.DONE);
  }

  public void start() {
    //    if (sprint.getStatus() != Sprint.Status.STARTED) {
    //      throw new IllegalStateException();
    //    }
    if (getStatus() != Status.CREATED) {
      throw new IllegalStateException("Item already started");
    }
    setStatus(Status.STARTED);
  }

  public boolean isDone() {
    return status == Status.DONE;
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

  public BacklogItem setStatus(Status status) {
    this.status = status;
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
