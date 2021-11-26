package victor.training.ddd.agile.domain.entity;

import victor.training.ddd.agile.domain.event.BacklogItemCompletedEvent;
import victor.training.ddd.common.events.DomainEventsPublisher;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.EnumType.STRING;

@Entity
public class SprintBacklogItem {
   @Id
   @GeneratedValue
   private Long id;
   private Long backlogItemId; // TODO add FK

   private Integer fpEstimation;
   private int hoursConsumed;
   @Enumerated(STRING)
   private Status status = Status.CREATED;

   public enum Status {
      CREATED,
      STARTED,
      DONE
   }

   public Long id() {
      return id;
   }

   public Long backlogItemId() {
      return backlogItemId;
   }

   public int hoursConsumed() {
      return hoursConsumed;
   }

   public Status status() {
      return status;
   }

   public Integer fpEstimation() {
      return fpEstimation;
   }

   protected SprintBacklogItem() {
   }

   public SprintBacklogItem(Long backlogItemId, Integer fpEstimation) {
      this.backlogItemId = backlogItemId;
      this.fpEstimation = fpEstimation;
   }

   public void start() {
      if (status != Status.CREATED) {
         throw new IllegalStateException("Item already started");
      }
      status = Status.STARTED;
   }

   public void finish() {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Cannot complete an Item before starting it");
      }
      status = Status.DONE;
      DomainEventsPublisher.publish(new BacklogItemCompletedEvent(backlogItemId));
   }

   public boolean isDone() {
      return status() == Status.DONE;
   }

   void addHours(int hours) {
      if (hours < 0) {
         throw new IllegalArgumentException("ce faci frate, trisezi ?!");
      }
      hoursConsumed += hours;
   }


}
