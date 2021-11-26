package victor.training.ddd.agile.domain.entity;

import lombok.*;
import org.springframework.beans.factory.annotation.Configurable;
import victor.training.ddd.agile.domain.event.BlackSprintEvent;
import victor.training.ddd.agile.domain.event.SprintFinishedEarlierEvent;
import victor.training.ddd.common.events.DomainEventsPublisher;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;
import static javax.persistence.EnumType.STRING;

@Getter
@NoArgsConstructor
@Entity
@Configurable
public class Sprint {
   @EmbeddedId
   private SprintId id;
   private Integer iteration;
   private LocalDate plannedEnd;

   @Setter
   private LocalDate start;
   @Setter
   private LocalDate end;
   @Enumerated(STRING)
   private Status status = Status.CREATED;
   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
   @JoinColumn
   private List<SprintBacklogItem> items = new ArrayList<>();

   public Sprint(SprintId id, LocalDate plannedEnd) {
      this.id = id;
      this.plannedEnd = plannedEnd;
      this.iteration = id.iteration();
   }

   public void finishSprint(SprintId id) {
      if (status != Status.STARTED) {
         throw new IllegalStateException();
      }
      end = LocalDate.now();
      status = Status.FINISHED;

      List<Long> notDoneItemIds = getItems().stream()
          .filter(not(SprintBacklogItem::isDone))
          .map(SprintBacklogItem::backlogItemId)
          .collect(toList());

      if (notDoneItemIds.size() >= 1) {
         DomainEventsPublisher.publish(new BlackSprintEvent(id.productCode(), notDoneItemIds));
      }
   }

   public boolean finishedEarlier() {
      return getItems().stream().allMatch(SprintBacklogItem::isDone)
             && LocalDate.now().isBefore(getPlannedEnd());
   }

   public int getIteration() {
      return iteration;
   }

   public SprintBacklogItem addItem(long backlogId, int fpEstimation) {
      if (status != Status.CREATED) {
         throw new IllegalStateException("Can only add items to Sprint before it starts");
      }
      SprintBacklogItem item = new SprintBacklogItem(backlogId, fpEstimation);
      getItems().add(item);
      return item;
   }

   public void startItem(long backlogId) {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      itemById(backlogId).start();

   }

   private SprintBacklogItem itemById(long backlogId) {
      return items.stream().filter(i -> i.id().equals(backlogId)).findFirst().get();
   }

   public void start() {
      if (getStatus() != Status.CREATED) {
         throw new IllegalStateException();
      }
      setStart(LocalDate.now());
      this.status = Status.STARTED;
   }

   public boolean isFinished() {
      return getStatus() != Status.FINISHED;
   }

   public void logHours(long backlogId, int hours) {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      if (itemById(backlogId).status() != SprintBacklogItem.Status.STARTED) {
         throw new IllegalStateException("Item not started");
      }
      itemById(backlogId).addHours(hours);
   }

   public void completeItem(long backlogId) {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      SprintBacklogItem sprintBacklogItem = itemById(backlogId);
      sprintBacklogItem.finish();

      if (finishedEarlier()) {
         DomainEventsPublisher.publish(new SprintFinishedEarlierEvent(id));
//         eventPublisher.publishEvent(new SprintFinishedEarlierEvent(id));
      }
   }

   public enum Status {
      CREATED,
      STARTED,
      FINISHED
   }
}


