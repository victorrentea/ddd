package victor.training.ddd.agile.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import victor.training.ddd.agile.common.DomainEvents;
import victor.training.ddd.agile.event.SprintCompletedEvent;
import victor.training.ddd.agile.service.EmailService;
import victor.training.ddd.agile.service.MailingListClient;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Sprint {
   @Id
   @GeneratedValue
   private Long id;
   private int iteration;
   @ManyToOne
   // TODO replace
   private Product product; // RULE: do not keep object links to other agg = Change contention
   // ==> a method in an aggragate can only be:
   // 1) a query method returining data
   // 2) change data inside = command
   // 2b) more side effects to other Agg / outside world (eg email send)    -> fire Event.
   // with
   private Long productId;
   private LocalDate startDate;
   private LocalDate plannedEndDate;
   private LocalDate endDate;

   public boolean allItemsAreDone() {
      return this.items.stream().allMatch(BacklogItem::isDone);
   }

   public void method() {
      product.setName("whatever");// PANIC moment; the callers of sprint.method()
      // do NOT expect the Product to change
   }

   public void startItem(long backlogId/*, BacklogItemRepo omg*/) {
      if (status != Status.STARTED) {
         throw new IllegalStateException();
      }
      itemById(backlogId).start();
   }


   public void complete(long backlogId/*, BacklogItemRepo omg*/) {
      if (status != Status.STARTED) {
         throw new IllegalStateException();
      }
      itemById(backlogId).complete();

      // option 3: pass services as arg to methods of agg < weird.
      if (allItemsAreDone()) {
         DomainEvents.publishEvent(new SprintCompletedEvent(id));
      }
   }

   private BacklogItem itemById(long backlogId) {
      return items.stream().filter(i -> i.getId().equals(backlogId)).findFirst().orElseThrow();
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

