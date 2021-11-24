package victor.training.ddd.agile;

import lombok.*;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.common.events.DomainEvent;
import victor.training.ddd.common.events.DomainEventsPublisher;
import victor.training.ddd.common.repo.CustomJpaRepository;
import victor.training.ddd.varie.Email;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.function.Predicate.not;
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
   private final SprintMetricsCalculator sprintMetricsCalculator;
   private final SprintBacklogItemRepo sprintBacklogItemRepo;
   private final MailingListService mailingListService;
   private final ApplicationEventPublisher eventPublisher;

   @PostMapping("sprint")
   public SprintId createSprint(@RequestBody SprintDto dto) {
      Product product = productRepo.findOneById(dto.productId);

      int iteration = product.nextIterationNumber();
      SprintId sprintId = new SprintId(product.getCode(), iteration);
      Sprint sprint = new Sprint(sprintId, dto.plannedEnd);
      return sprintRepo.save(sprint).getId();
   }

   @GetMapping("sprint/{id}")
   public Sprint getSprint(@PathVariable SprintId id) { // TODO return SprintDto nu entity in afara ca ii cuplezi pe dushmani (FE/clientI) la modelul tau intern
      return sprintRepo.findOneById(id);
   }

   @PostMapping("sprint/{id}/start")
   public void startSprint(@PathVariable SprintId id) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.start();
   }

   @PostMapping("sprint/{id}/finish")
   public void finishSprint(@PathVariable SprintId id) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.finishSprint(id);
   }

   @EventListener
   public void onSprint(BlackSprintEvent event) {
      List<BacklogItem> productBacklogItems = backlogItemRepo.findAllById(event.getNotDoneItemIds());
      List<String> notDoneTitles = productBacklogItems.stream().map(BacklogItem::getTitle).collect(toList());
      Product product = productRepo.findByCode(event.getProductCode());
      emailService.sendNotDoneItemsDebrief(product.getOwnerEmail(), notDoneTitles);
   }

   @GetMapping("sprint/{id}/metrics")
   public SprintMetrics getSprintMetrics(@PathVariable SprintId id) {
      Sprint sprint = sprintRepo.findOneById(id);
      if (sprint.isFinished()) {
         throw new IllegalStateException();
      }
      return sprintMetricsCalculator.computeMetrics(sprint);
   }

   @PostMapping("sprint/{sprintId}/add-item")
   public void addItem(@PathVariable SprintId sprintId, @RequestBody AddSprintBacklogItemRequest request) {
      Sprint sprint = sprintRepo.findOneById(sprintId);
      SprintBacklogItem item = sprint.addItem(request.backlogId, request.fpEstimation);
      sprintBacklogItemRepo.save(item); // auto-flush de modificari facute pe entitati in cadrul unei tranzactii
   }

   @PostMapping("sprint/{id}/start-item/{backlogId}")
   public void startItem(@PathVariable SprintId id, @PathVariable long backlogId) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.startItem(backlogId);
   }

   @Transactional
   @PostMapping("sprint/{id}/complete-item/{backlogId}")
   public void completeItem(@PathVariable SprintId id, @PathVariable long backlogId) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.completeItem(backlogId);
   }

   @EventListener
//   @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
   public void method(SprintFinishedEarlierEvent earlierEvent) {
      Product product = productRepo.findByCode(earlierEvent.getSprintId().productCode());
      sendEarlyFinishEmail(product);

   }

   private void sendEarlyFinishEmail(Product product) {
      String productCode = null; // sprint.getProduct().getCode()
      System.out.println("Sending CONGRATS email to team of product " + productCode +
                         ": They finished the items earlier. They have time to mob refactor! (OMG!)");
      List<Email> emails = mailingListService.retrieveEmails(product.getTeamMailingList());
      emailService.sendCongratsEmail(emails);
   }

   @PostMapping("sprint/{id}/log-hours")
   public void logHours(@PathVariable SprintId id, @RequestBody LogHoursRequest request) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.logHours(request.backlogId, request.hours);
   }

   @Data
   static class SprintDto {
      public Long productId;
      public LocalDate plannedEnd;
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

   @Data
   static class AddSprintBacklogItemRequest {
      public long backlogId;
      public int fpEstimation;
   }

   @Data
   static class LogHoursRequest {
      public long backlogId;
      public int hours;
   }

}

@Entity
class SprintBacklogItem {
   @Id
   @GeneratedValue
   private Long id;
   private Long backlogItemId; // TODO add FK

   private Integer fpEstimation;
   private int hoursConsumed;
   @Enumerated(STRING)
   private Status status = Status.CREATED;

   public enum Status {
      CREATED,
      STARTED,
      DONE
   }

   public Long id() {
      return id;
   }

   public Long backlogItemId() {
      return backlogItemId;
   }

   public int hoursConsumed() {
      return hoursConsumed;
   }

   public Status status() {
      return status;
   }

   public Integer fpEstimation() {
      return fpEstimation;
   }

   protected SprintBacklogItem() {
   }

   public SprintBacklogItem(Long backlogItemId, Integer fpEstimation) {
      this.backlogItemId = backlogItemId;
      this.fpEstimation = fpEstimation;
   }

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
      return status() == Status.DONE;
   }

   void addHours(int hours) {
      if (hours < 0) {
         throw new IllegalArgumentException("ce faci frate, trisezi ?!");
      }
      hoursConsumed += hours;
   }


}

@Embeddable
@Data
class SprintId implements Serializable {
   private String id;

   protected SprintId() {
   }

   public SprintId(String productCode, int iteration) {
      if (productCode.length() != 3) {
         throw new IllegalArgumentException();
      }
      if (iteration < 0) {
         throw new IllegalArgumentException();
      }
      id = productCode + "-" + iteration;
   }

   public SprintId(String id) {
      if (!id.matches("\\w{3}-\\d+")) {
         throw new IllegalArgumentException("Invalid format");
      }
      this.id = id;
   }

   public String productCode() {
      return id.split("-")[0];
   }

   public int iteration() {
      return Integer.parseInt(id.split("-")[1]);
   }
}

@Getter
@NoArgsConstructor
@Entity
@Configurable
class Sprint {
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
   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

      DomainEventsPublisher.publish(new BacklogItemCompletedEvent(sprintBacklogItem.backlogItemId()));
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

@Value
class SprintFinishedEarlierEvent implements DomainEvent {
   SprintId sprintId;
}
@Value
class BacklogItemCompletedEvent implements DomainEvent {
   Long backlogItemId;
}

interface SprintBacklogItemRepo extends CustomJpaRepository<SprintBacklogItem, Long> {
}

@Value
class BlackSprintEvent implements DomainEvent {
   String productCode;
   List<Long> notDoneItemIds;
}


interface SprintRepo extends CustomJpaRepository<Sprint, SprintId> {
}
