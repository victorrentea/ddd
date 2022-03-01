package victor.training.ddd.agile.domain.model;

import org.springframework.data.domain.AbstractAggregateRoot;
import victor.training.ddd.agile.domain.event.ItemAddedEvent;
import victor.training.ddd.agile.domain.event.SprintFinishedEvent;
import victor.training.ddd.common.DDD.AggregateRoot;
import victor.training.ddd.common.events.DomainEvents;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;

@AggregateRoot
@Entity
// AggregateRoot is responsible to enforce all contraints spanning bETWEEN the entities inside this Aggregate
public class Sprint extends AbstractAggregateRoot<Sprint> {
   @Id
   @GeneratedValue
   private Long id;
   private int iteration;

   // Reference other Aggregates via id (not via object links)
   private Long productId;

   private LocalDate start;
   private LocalDate plannedEnd;
   private LocalDate end;

   public enum Status {
      CREATED,
      STARTED,
      FINISHED
   }

   @Enumerated(STRING)
   private Status status = Status.CREATED;

   @OneToMany(cascade =ALL, orphanRemoval = true,
       fetch = FetchType.EAGER // even if it's a bad practice, you may want this to avoid LAZY LOADING > allow you to NOT keep @Transaction one
   ) // unidirectional // TODO Tue
   @JoinColumn // adds a SPRINT_ID column to BACKLOG_ITEM table.!!!
   private List<SprintBacklogItem> items = new ArrayList<>();

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

   public List<SprintBacklogItem> getItems() {
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

   public void finish() {
      if (status != Status.STARTED) {
         throw new IllegalStateException();
      }
      status = Status.FINISHED;
      end = LocalDate.now();

      DomainEvents.publishEvent(new SprintFinishedEvent(id)); // more reusable
//      registerEvent(new SprintFinishedEvent(id));
   }

   // banning to keep references to other Aggregates and/or Spring manged beans *Service/Repo
   // aims to make calling methods in an aggregate SAFE. The worse that can happen is
   // 1 changes to fields of the aggregate
   // 2 a Domain Event might have been published.
//   @ManyToOne
//   private Product product;
//   @Autowired
//   private SprintRepo sprintRepo;

   public void startItem(String backlogId) {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      backlogItemById(backlogId).start();
   }

   public SprintBacklogItem backlogItemById(String backlogId) {
      return items.stream().filter(it -> Objects.equals(it.getId(), backlogId)).findFirst().orElseThrow();
   }

   public void completeItem(String backlogId) {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      SprintBacklogItem item = backlogItemById(backlogId);
      item.complete();
   }

   public void logHours(String backlogId, int hours) {
      SprintBacklogItem item = backlogItemById(backlogId);
      if (status != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      item.addHours(hours);
   }

   public List<SprintBacklogItem> getItemsNotDone() {
      return getItems().stream()
          .filter(not(SprintBacklogItem::isDone))
           .collect(toList());
   }

   public void addItem(SprintBacklogItem item) {
      if (status != Status.CREATED) {
         throw new IllegalStateException("Can only add items to Sprint before it starts");
      }
      items.add(item);
      DomainEvents.publishEvent(new ItemAddedEvent(item.getId()));

   }

   public boolean allItemsDone() {
      return getItems().stream().allMatch(item -> item.getStatus() == SprintBacklogItem.Status.DONE);
   }


}


// SPRINT ()

// BACKLOG_ITEM ()

