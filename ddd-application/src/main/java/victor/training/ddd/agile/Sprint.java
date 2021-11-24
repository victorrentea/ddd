package victor.training.ddd.agile;

import lombok.*;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.Sprint.Status;
import victor.training.ddd.common.events.DomainEvent;
import victor.training.ddd.common.events.DomainEventsPublisher;
import victor.training.ddd.common.repo.CustomJpaRepository;
import victor.training.ddd.varie.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static javax.persistence.EnumType.STRING;

@Transactional
@RestController
@RequiredArgsConstructor
class SprintController {
   private final SprintRepo sprintRepo;
   private final ProductRepo productRepo;
   private final BacklogItemRepo backlogItemRepo;
   private final EmailService emailService;

   @Data
   static class SprintDto {
      public Long productId;
      public LocalDate plannedEnd;
   }

   @PostMapping("sprint")
   public Long createSprint(@RequestBody SprintDto dto) {
      Product product = productRepo.findOneById(dto.productId);
      Sprint sprint = new Sprint() // TODO constr arg
          .setIteration(product.incrementAndGetIteration())
          .setProduct(product)
          .setPlannedEnd(dto.plannedEnd);
      return sprintRepo.save(sprint).getId();
   }

   @GetMapping("sprint/{id}")
   public Sprint getSprint(@PathVariable long id) { // TODO return SprintDto nu entity in afara ca ii cuplezi pe dushmani (FE/clientI) la modelul tau intern
      return sprintRepo.findOneById(id);
   }

   @PostMapping("sprint/{id}/start")
   public void startSprint(@PathVariable long id) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.start();
   }

   @PostMapping("sprint/{id}/end")
   public void endSprint(@PathVariable long id) {
      Sprint sprint = sprintRepo.findOneById(id);
      // BAGAM EVENTURI1
      if (sprint.getStatus() != Status.STARTED) {
         throw new IllegalStateException();
      }
      sprint.setEnd(LocalDate.now());
      sprint.setStatus(Status.FINISHED);

      List<SprintBacklogItem> notDone = sprint.getItems().stream()
          .filter(item -> item.getStatus() != SprintBacklogItem.Status.DONE)
          .collect(toList());

      if (notDone.size() >= 1) {
         List<BacklogItem> productBacklogItems = backlogItemRepo.findAllById(notDone.stream()
             .map(SprintBacklogItem::getBacklogItemId)
             .collect(toList()));
         List<String> notDoneTitles = productBacklogItems.stream().map(BacklogItem::getTitle).collect(toList());
         emailService.sendNotDoneItemsDebrief(sprint.getProduct().getOwnerEmail(), notDoneTitles);
      }
   }

   @Data
   static class SprintMetrics {
      public int consumedHours;
      public int doneFP;
      public double fpVelocity;
      public int hoursConsumedForNotDone;
      public int calendarDays;
      public int delayDays;
   }

   @GetMapping("sprint/{id}/metrics")
   public SprintMetrics getSprintMetrics(@PathVariable long id) {
      Sprint sprint = sprintRepo.findOneById(id);
      if (sprint.isFinished()) {
         throw new IllegalStateException();
      }
      return sprintMetricsCalculator.computeMetrics(sprint);
   }

   private final SprintMetricsCalculator sprintMetricsCalculator;

   private final SprintBacklogItemRepo sprintBacklogItemRepo;

   @Data
   static class AddSprintBacklogItemRequest {
      public long backlogId;
      public int fpEstimation;
   }

   @PostMapping("sprint/{sprintId}/add-item")
   public void addItem(@PathVariable long sprintId, @RequestBody AddSprintBacklogItemRequest request) {
      Sprint sprint = sprintRepo.findOneById(sprintId);
      SprintBacklogItem item = sprint.addItem(request.backlogId, request.fpEstimation);
      sprintBacklogItemRepo.save(item); // auto-flush de modificari facute pe entitati in cadrul unei tranzactii
   }


   @PostMapping("sprint/{id}/start-item/{backlogId}")
   public void startItem(@PathVariable long id, @PathVariable long backlogId) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.startItem(backlogId);
   }

   private final MailingListService mailingListService;
   private final ApplicationEventPublisher eventPublisher;

   @Transactional
   @PostMapping("sprint/{id}/complete-item/{backlogId}")
   public void completeItem(@PathVariable long id, @PathVariable long backlogId) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.completeItem(backlogId);
      // tot asa si daca aveai de modificat 2 agregate: Product = productRepo.findOneById()product.mutatii();

//      if (sprint.finishedEarlier()) {
//         sendEarlyFinishEmail(sprint);
//      }
   }

   @EventListener
//   @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
   public void method(SprintFinishedEarlierEvent earlierEvent) {
      Sprint sprint = sprintRepo.findOneById(earlierEvent.getSprintId());
      sendEarlyFinishEmail(sprint);

   }

   private void sendEarlyFinishEmail(Sprint sprint) {
      String productCode = null; // sprint.getProduct().getCode()
      System.out.println("Sending CONGRATS email to team of product " + productCode +
                         ": They finished the items earlier. They have time to mob refactor! (OMG!)");
      List<Email> emails = mailingListService.retrieveEmails(sprint.getProduct().getTeamMailingList());
      emailService.sendCongratsEmail(emails);
   }

   private void checkSprintMatchesAndStarted(long id, SprintBacklogItem backlogItem) {
      // TODO
//      if (!backlogItem.getSprint().getId().equals(id)) {
//         throw new IllegalArgumentException("item not in sprint");
//      }
      Sprint sprint = sprintRepo.findOneById(id);
      if (sprint.getStatus() != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
   }

   @Data
   static class LogHoursRequest {
      public long backlogId;
      public int hours;
   }

   @PostMapping("sprint/{id}/log-hours")
   public void logHours(@PathVariable long id, @RequestBody LogHoursRequest request) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.logHours(request.backlogId, request.hours);
   }

}

@Getter
@Entity
class SprintBacklogItem {
   @Id
   @GeneratedValue
   private Long id;
   private Long backlogItemId; // TODO add FK

   private Integer fpEstimation;
   private int hoursConsumed;

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
   }

   public boolean isDone() {
      return getStatus() == Status.DONE;
   }

   public enum Status {
      CREATED,
      STARTED,
      DONE
   }

   @Enumerated(STRING)
   private Status status = Status.CREATED;

   void addHours(int hours) {
      hoursConsumed += hours;
   }

   protected SprintBacklogItem() {
   }

   public SprintBacklogItem(Long backlogItemId, Integer fpEstimation) {
      this.backlogItemId = backlogItemId;
      this.fpEstimation = fpEstimation;
   }
}

interface SprintBacklogItemRepo extends CustomJpaRepository<SprintBacklogItem, Long> {
}

@Getter
@Setter
@NoArgsConstructor
@Entity
@Configurable
class Sprint {
   @Id
   @GeneratedValue
   private Long id;
   private int iteration;
   @ManyToOne
   private Product product;
   private LocalDate start;
   private LocalDate plannedEnd;
   private LocalDate end;

   public boolean finishedEarlier() {
      return getItems().stream().allMatch(SprintBacklogItem::isDone)
             && LocalDate.now().isBefore(getPlannedEnd());
   }

   public enum Status {
      CREATED,
      STARTED,
      FINISHED
   }

   @Enumerated(STRING)
   private Status status = Status.CREATED;

   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   @JoinColumn
   private List<SprintBacklogItem> items = new ArrayList<>();


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
      return items.stream().filter(i -> i.getId().equals(backlogId)).findFirst().get();
   }

   public void start() {
      if (getStatus() != Status.CREATED) {
         throw new IllegalStateException();
      }
      setStart(LocalDate.now());
      setStatus(Status.STARTED);
   }

   public boolean isFinished() {
      return getStatus() != Status.FINISHED;
   }

   public void logHours(long backlogId, int hours) {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      if (itemById(backlogId).getStatus() != SprintBacklogItem.Status.STARTED) {
         throw new IllegalStateException("Item not started");
      }
      itemById(backlogId).addHours(hours);
   }

   public void completeItem(long backlogId) {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      itemById(backlogId).finish();

      if (finishedEarlier()) {
         DomainEventsPublisher.publish(new SprintFinishedEarlierEvent(id));
//         eventPublisher.publishEvent(new SprintFinishedEarlierEvent(id));
      }
   }
}

@Value
class SprintFinishedEarlierEvent implements DomainEvent {
   long sprintId;
}


interface SprintRepo extends CustomJpaRepository<Sprint, Long> {
}
