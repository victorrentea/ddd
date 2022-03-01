package victor.training.ddd.agile.domain.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.UUID;

import static javax.persistence.EnumType.STRING;

@Entity
// Child Entity of the Sprint Aggregate
@NoArgsConstructor(access = AccessLevel.PRIVATE)
//@Table(uniqueConstraints = {@UniqueConstraint()})
public class SprintBacklogItem {
//   @Id
//   @GeneratedValue
//   private Long id;
   @Id
   private String id = UUID.randomUUID().toString();
   //a: the involvment of a ProductBacklogItem in a Sprint
      // title description are preserved on PBI
      // a productBacklogItemId stored on the SprintBacklogId
   private Long productBacklogItemId;

//   private String tenantId;
//   private String externalId;

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
   private Integer fpEstimation;
   private Integer hoursConsumed = 0;


   public SprintBacklogItem(long productBacklogItemId, int fpEstimation) {
      this.productBacklogItemId = productBacklogItemId;
      this.fpEstimation = fpEstimation;
   }

   public SprintBacklogItem setId(String id) {
      this.id = id;
      return this;
   }

   public Long getProductBacklogItemId() {
      return productBacklogItemId;
   }


   public String getId() {
      return id;
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
