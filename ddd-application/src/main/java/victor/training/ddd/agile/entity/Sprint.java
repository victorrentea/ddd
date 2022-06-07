package victor.training.ddd.agile.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.AbstractAggregateRoot;
import victor.training.ddd.agile.common.DomainEvents;
import victor.training.ddd.agile.events.SprintFinishedEvent;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
//   @ManyToOne
//   private Product product;
   private Long productId;

   private LocalDate startDate;
   private LocalDate plannedEndDate;
   private LocalDate endDate;

   @Enumerated(STRING)
   private Status status = Status.CREATED;

   @OneToMany(mappedBy = "sprint")
   private List<BacklogItem> items = new ArrayList<>();

   public Sprint() {
   }

   public Long getProductId() {
      return productId;
   }

   public Sprint setProductId(Long productId) {
      this.productId = productId;
      return this;
   }

   public Long getId() {
      return this.id;
   }

   public int getIteration() {
      return this.iteration;
   }

//   public Product getProduct() {
//      return this.product;
//   }

   public LocalDate getStartDate() {
      return this.startDate;
   }

   public LocalDate getPlannedEndDate() {
      return this.plannedEndDate;
   }

   public LocalDate getEndDate() {
      return this.endDate;
   }

   public Status getStatus() {
      return this.status;
   }

   public List<BacklogItem> getItems() {
      return this.items;
   }

   public Sprint setId(Long id) {
      this.id = id;
      return this;
   }

   public Sprint setIteration(int iteration) {
      this.iteration = iteration;
      return this;
   }

//   public Sprint setProduct(Product product) {
//      this.product = product;
//      return this;
//   }

   public Sprint setStartDate(LocalDate startDate) {
      this.startDate = startDate;
      return this;
   }

   public Sprint setPlannedEndDate(LocalDate plannedEndDate) {
      this.plannedEndDate = plannedEndDate;
      return this;
   }

   public Sprint setEndDate(LocalDate endDate) {
      this.endDate = endDate;
      return this;
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

   public Sprint setItems(List<BacklogItem> items) {
      this.items = items;
      return this;
   }
   public boolean isFinished() {
      return status == Status.FINISHED;
   }

   public void startItem(long backlogId) {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      BacklogItem backlogItem = itemById(backlogId);
      backlogItem.start();
   }

   public void completeItem(long backlogId) {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      BacklogItem backlogItem = itemById(backlogId);
      backlogItem.completeItem();
      if (items.stream().allMatch(BacklogItem::isDone)) {
//         DomainEvents.publishEvent(new SprintFinishedEvent(id)); //>50%
         registerEvent(new SprintFinishedEvent(id));
      }
      log.debug("AFter event fired");
//         System.out.println("Sending CONGRATS email to team of product " + product.getCode()
//                            + ": They finished the items earlier. They have time to refactor! (OMG!)");
//         if (product.getTeamMailingList().isPresent()) {
//            List<String> emails = mailingListClient.retrieveEmails(product.getTeamMailingList().get()); //2
//            emailService.sendCongratsEmail(emails); //3
//         }
//      }
   }

//   @Autowired
//   ApplicationEventPublisher publisher;
   public boolean allItemsAreDone() {
      return this.items.stream().allMatch(BacklogItem::isDone);
   }

   private BacklogItem itemById(long backlogId) {
      return items.stream()
              .filter(item -> item.getId().equals(backlogId))
              .findFirst()
              .orElseThrow();
   }
}

