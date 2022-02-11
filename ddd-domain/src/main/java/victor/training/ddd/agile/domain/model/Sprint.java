package victor.training.ddd.agile.domain.model;

import victor.training.ddd.common.DDD;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static javax.persistence.EnumType.STRING;


@Entity
@DDD.AggregateRoot
public class Sprint {
   @Id
   @GeneratedValue
   private Long id;
   private int iterationNumber;
//   @ManyToOne
//   private Product product;
   private Long productId;
   private LocalDate start;
   private LocalDate plannedEnd;
   private LocalDate end;
   @Enumerated(STRING)
   private Status status = Status.CREATED;

//   @JoinColumn
   @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
   private List<SprintBacklogItem> items = new ArrayList<>();


   public Sprint() {
   }



   public Long getId() {
      return id;
   }

   public int getIterationNumber() {
      return iterationNumber;
   }

   public Long getProductId() {
      return productId;
   }

   public Sprint setProductId(Long productId) {
      this.productId = productId;
      return this;
   }

   public LocalDate getStart() {
      return start;
   }

   public LocalDate getPlannedEnd() {
      return plannedEnd;
   }

   public LocalDate getEnd() {
      return end;
   }

   public Status getStatus() {
      return status;
   }

   public List<SprintBacklogItem> getItems() {
      return Collections.unmodifiableList(items);
   }

   public Sprint setId(Long id) {
      this.id = id;
      return this;
   }

   public Sprint setIterationNumber(int iteration) {
      iterationNumber = iteration;
      return this;
   }


   public Sprint setStart(LocalDate start) {
      this.start = start;
      return this;
   }

   public Sprint setPlannedEnd(LocalDate plannedEnd) {
      this.plannedEnd = plannedEnd;
      return this;
   }

   public Sprint setEnd(LocalDate end) {
      this.end = end;
      return this;
   }

   public Sprint setStatus(Status status) {
      this.status = status;
      return this;
   }

   public void addItem(long productBacklogItemId, int fpEstimation) {
      if (getStatus() != Status.CREATED) {
         throw new IllegalStateException("Can only add items to Sprint before it starts");
      }
      SprintBacklogItem sprintBacklogItem = new SprintBacklogItem(id, productBacklogItemId, fpEstimation);
      items.add(sprintBacklogItem);
   }

   public void startItem(long sprintBacklogItemId) {
      checkSprintStarted();
      itemById(sprintBacklogItemId).start();
   }

   public boolean completeItem(long sprintBacklogItemId) {
      checkSprintStarted();
      itemById(sprintBacklogItemId).complete();
      return getItems().stream().allMatch(item -> item.getStatus() == SprintBacklogItem.Status.DONE);
   }

   public void logHours(long sprintBacklogItemId, int hours) {
      checkSprintStarted();
      itemById(sprintBacklogItemId).addHours(hours);
   }

   private SprintBacklogItem itemById(long sprintBacklogItemId) {
      return items.stream()
          .filter(item -> item.getProductBacklogItemId().equals(sprintBacklogItemId))
          .findFirst()
          .get();
   }

   public void checkSprintStarted() {
      if (getStatus() != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
   }


   public enum Status {
      CREATED,
      STARTED,
      FINISHED
   }


}

