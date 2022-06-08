package victor.training.ddd.agile.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AbstractAggregateRoot;
import victor.training.ddd.agile.common.DomainEvents;
import victor.training.ddd.agile.events.BacklogItemAddedToSprintEvent;
import victor.training.ddd.agile.events.SprintFinishedEvent;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static javax.persistence.EnumType.STRING;

//@Configurable // DON'T USE ME! allows @Autowired to work in @Entity.
@Slf4j
@Entity // Aggregate Root
public class Sprint extends AbstractAggregateRoot<Sprint> {
   public enum Status {
      CREATED,
      STARTED,
      FINISHED
   }

   @Id
   @GeneratedValue
   private Long id;
   private int iteration;
   private Long productId;

   private LocalDate startDate;
   private LocalDate plannedEndDate;
   private LocalDate endDate;

   @Enumerated(STRING)
   private Status status = Status.CREATED;


   // typical for AggregateRoot- child entity relation
   @JoinColumn
   @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
   private List<SprintItem> items = new ArrayList<>();

   protected Sprint() {
   }

   public Sprint(long productId, int iteration, LocalDate plannedEndDate) {
      this.iteration = iteration;
      this.productId = productId;
      this.plannedEndDate = Objects.requireNonNull(plannedEndDate);
   }

   public Long getProductId() {
      return productId;
   }

   public Long getId() {
      return id;
   }

   public int getIteration() {
      return iteration;
   }

   public LocalDate getStartDate() {
      return startDate;
   }

   public LocalDate getPlannedEndDate() {
      return plannedEndDate;
   }

   public LocalDate getEndDate() {
      return endDate;
   }

   public Status getStatus() {
      return status;
   }

   public List<SprintItem> getItems() {
      return Collections.unmodifiableList(items);
   }

   public String addItem(BacklogItem backlogItem, int fpEstimation) {
      if (status != Status.CREATED) {
         throw new IllegalStateException("Can only add items to Sprint before it starts");
      }
      SprintItem sprintItem = new SprintItem(backlogItem.getTitle(), backlogItem.getDescription(), fpEstimation);
      items.add(sprintItem);
      BacklogItemAddedToSprintEvent event = new BacklogItemAddedToSprintEvent(backlogItem.getId());
      DomainEvents.publishEvent(event);
      return sprintItem.getId();
   }

   public void start() { // a simple state machine checking the preconditions of the transition
      // and marking the audit column automatically
      if (status != Status.CREATED) {
         throw new IllegalStateException();
      }
      startDate = LocalDate.now();
      status = Status.STARTED;
   }

   public void end() {
      if (status != Status.STARTED) {
         throw new IllegalStateException();
      }
      endDate = LocalDate.now();
//      DomainEvents.publishEvent(new SprintEndedEvent(id));
      status = Status.FINISHED;
   }
   // Any downsides?
   // TESTING becomes harder. You can't just set the state to X, and how would I mock now() call.
// the problem with testing is when in one test you want to use a FINISHED
// Spring to pass it as a param to soem tested method. You can't just new Sprint().setState(FINISHED);
   // you now have to respect the state transaction:
   // s = new Sprint(); s.start(); s.end();
   // Idea: TestData.aStartedSprint()

   public boolean isFinished() {
      return status == Status.FINISHED;
   }


   public void logHours(String sprintItemId, int hours) {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      SprintItem sprintItem = itemById(sprintItemId);
      sprintItem.logHours(hours);
   }

   public void startItem(String sprintItemId) {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      SprintItem sprintItem = itemById(sprintItemId);
      sprintItem.start();
   }

   public void completeItem(String sprintItemId) {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      SprintItem sprintItem = itemById(sprintItemId);
      sprintItem.completeItem();
      if (items.stream().allMatch(SprintItem::isCompleted)) {
//         DomainEvents.publishEvent(new SprintFinishedEvent(id)); //>50%
         registerEvent(new SprintFinishedEvent(id));
      }
   }

   private SprintItem itemById(String sprintItemId) {
      return items.stream()
              .filter(item -> item.getId().equals(sprintItemId))
              .findFirst()
              .orElseThrow();
   }
}

