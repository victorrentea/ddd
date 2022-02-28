package victor.training.ddd.agile.domain.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;
import static javax.persistence.EnumType.STRING;



@Entity
// AggregateRoot is responsible to enforce all contraints spanning bETWEEN the entities inside this Aggregate
public class Sprint {
   @Id
   @GeneratedValue
   private Long id;
   private int iteration;

   // Reference other Aggregates via id (not via object links)
   private Long productId;

   private LocalDate start;
   private LocalDate plannedEnd;
   private LocalDate end;

   private Sprint() {} // just for Hibernate - it still works as private

   public Sprint(long productId, int iteration) {
      this.productId = productId;
      this.iteration = iteration;
   }

   public Long getProductId() {
      return productId;
   }

   public Sprint setProductId(Long productId) {
      this.productId = productId;
      return this;
   }

   public Long getId() {
      return id;
   }

   public int getIteration() {
      return iteration;
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

   public List<BacklogItem> getItems() {
      return items;
   }

   public Sprint setId(Long id) {
      this.id = id;
      return this;
   }

   public Sprint setIteration(int iteration) {
      this.iteration = iteration;
      return this;
   }


   public Sprint setPlannedEnd(LocalDate plannedEnd) {
      this.plannedEnd = plannedEnd;
      return this;
   }

   public void start() {
      if (status != Status.CREATED) {
         throw new IllegalStateException();
      }
      status = Status.STARTED;
      start = LocalDate.now();
   }


   public List<BacklogItem> finish() {
      if (status != Status.STARTED) {
         throw new IllegalStateException();
      }
      status = Status.FINISHED;
      end = LocalDate.now();

      return  getItemsNotDone();
   }

   public void startItem(long backlogId) {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      backlogItemById(backlogId).start();
   }

   private BacklogItem backlogItemById(long backlogId) {
      return items.stream().filter(it -> it.getId() == backlogId).findFirst().orElseThrow();
   }

   public void completeItem(long backlogId) {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      BacklogItem backlogItem = backlogItemById(backlogId);
      backlogItem.complete();
   }

   public void logHours(long backlogId, int hours) {
      BacklogItem backlogItem = backlogItemById(backlogId);
      if (status != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      backlogItem.addHours(hours);
   }

   public List<BacklogItem> getItemsNotDone() {
      return getItems().stream()
          .filter(not(BacklogItem::isDone))
           .collect(toList());
   }

   public enum Status {
      CREATED,
      STARTED,
      FINISHED
   }

   @Enumerated(STRING)
   private Status status = Status.CREATED;

   @OneToMany(mappedBy = "sprint")
   private List<BacklogItem> items = new ArrayList<>();

}

