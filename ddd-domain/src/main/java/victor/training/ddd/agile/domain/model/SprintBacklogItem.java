package victor.training.ddd.agile.domain.model;

import lombok.Getter;
import victor.training.ddd.common.DDD;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import static javax.persistence.EnumType.STRING;

@DDD.Entity
@Entity
@Getter
public class SprintBacklogItem {
   public enum Status {
      CREATED,
      STARTED,
      DONE
   }
   @Id // it's a @OneToOne without an annotation
   private Long productBacklogItemId;
//   @Id // UNCOMMENT if you want a composite PK between spring,productitem >>> why? to allow the same productItem to be part of two sprints
   private Long sprintId;
   private Integer fpEstimation; // ⚠ not NULL when assigned to a sprint

   @Enumerated(STRING)
   private Status status = Status.CREATED;

   private int hoursConsumed;

   private SprintBacklogItem() {}

   public SprintBacklogItem(Long sprintId, Long productBacklogItemId, Integer fpEstimation) {
      this.productBacklogItemId = productBacklogItemId;
      this.sprintId = sprintId;
      this.fpEstimation = fpEstimation;
   }

   public void start() {
      if (status != Status.CREATED) {
         throw new IllegalStateException("Item already started");
      }
      status = Status.STARTED;
   }

   public void complete() {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Cannot complete an Item before starting it");
      }
      status = Status.DONE;
//      registerEvent(new BacklogItemCompletedEvent())
      // TODO Victor 2022-02-11: @ END
   }


   public void addHours(int hours) {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Item not started");
      }
      hoursConsumed += hours;
   }
}
