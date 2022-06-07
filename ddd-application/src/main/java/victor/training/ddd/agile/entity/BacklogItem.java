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
@Entity // child entity of the  Sprint Aggregate
public class BacklogItem {
   @Id
   @GeneratedValue
   private Long id;
   @ManyToOne
   private Product product;

@NotNull
   private String title;
   private String description;

   void start() {
        if (status != BacklogItem.Status.CREATED) {
            throw new IllegalStateException("Item already started");
        }
        this.status = Status.STARTED;
    }

    public void completeItem() {
       if (getStatus() != Status.STARTED) {
          throw new IllegalStateException("Cannot complete an Item before starting it");
       }
       setStatus(Status.DONE);
    }

    public boolean isDone() {
       return getStatus() == Status.DONE;
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
