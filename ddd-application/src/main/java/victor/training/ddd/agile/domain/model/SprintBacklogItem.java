package victor.training.ddd.agile.domain.model;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Entity
// Child Entity of the Sprint Aggregate
public class SprintBacklogItem {
   @Id
   @GeneratedValue
   private Long id;
   //a: the involvment of a ProductBacklogItem in a Sprint
      // title description are preserved on PBI
      // a productBacklogItemId stored on the SprintBacklogId
//   private Long productBacklogItemId;

   //b: the entire representation of a backlog item during Sprint
      // a SprintBacklogItem replaces a ProdcutBacklogItem
      // title+description in another entity (in 2 tables)
//   private String title;
//   private String description;
//   @Version
//   private Long version;

   public enum Status { // Sprint
      CREATED,
      STARTED,
      DONE
   }
   @Enumerated(STRING)
   private Status status = Status.CREATED;  // Sprint

   // Sprint
   @Column(nullable = false) // NOT NULL
   private Integer fpEstimation; // ⚠ not NULL when assigned to a sprint
   private Integer hoursConsumed; // ⚠ not NULL when assigned to a sprint


   public Long getId() {
      return id;
   }

   public SprintBacklogItem setFpEstimation(Integer fpEstimation) {
      this.fpEstimation = fpEstimation;
      return this;
   }

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
      if (status != Status.STARTED) {
         throw new IllegalStateException("Item not started");
      }
      hoursConsumed += hours;
   }

   public Status getStatus() {
      return status;
   }

   public Integer getFpEstimation() {
      return fpEstimation;
   }

   public Integer getHoursConsumed() {
      return hoursConsumed;
   }

}
