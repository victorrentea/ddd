package victor.training.ddd.agile.domain.model;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.EnumType.STRING;

@Entity
@Getter
public class SprintBacklogItem {
   public enum Status {
      CREATED,
      STARTED,
      DONE
   }
   @Id
   @GeneratedValue
   private Long id;
   private Long sprintId;
   private Integer fpEstimation; // ⚠ not NULL when assigned to a sprint

   @Enumerated(STRING)
   private Status status = Status.CREATED;

   private int hoursConsumed;

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
      hoursConsumed += hours;
   }
}
