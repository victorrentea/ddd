package victor.training.ddd.agile.domain.model;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@NoArgsConstructor
@Entity
// Child Entity of the Sprint Aggregate
public class BacklogItem {
   @Id
   @GeneratedValue
   private Long id;
   public enum Status {
      CREATED,
      STARTED,
      DONE
   }
   @Enumerated(STRING)
   private Status status = Status.CREATED;

   private Integer fpEstimation; // ⚠ not NULL when assigned to a sprint
   private Integer hoursConsumed; // ⚠ not NULL when assigned to a sprint

   @Version
   private Long version;
   @ManyToOne
   private Product product;
//   private Long productId;

   private String title;
   private String description;

   public void start() {
      if (status != Status.CREATED) {
         throw new IllegalStateException("Item already started");
      }
      status = Status.STARTED;
   }
   public void complete() {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Item already started");
      }
      status = Status.DONE;
   }

   public boolean isDone() {
      return status == Status.DONE;
   }


   public void addHours(int hours) {
      if (this.status != BacklogItem.Status.STARTED) {
         throw new IllegalStateException("Item not started");
      }
      hoursConsumed += hours;
   }

}

