package victor.training.ddd.agile.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@NoArgsConstructor
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

}
