package victor.training.ddd.agile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.Sprint.Status;
import victor.training.ddd.common.repo.CustomJpaRepository;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.EnumType.STRING;

@Transactional
@RestController
@RequiredArgsConstructor
class SprintService {
   private final SprintRepo sprintRepo;
   private final ProductRepo productRepo;
   private final BacklogItemRepo backlogItemRepo;
   private final EmailService emailService;

   @Data
   static class CreateSprintRequest {
      public Long productId;
      public LocalDate plannedEnd;
   }

   @PostMapping("sprint")
   public Long createSprint(@RequestBody CreateSprintRequest dto) {
      Product product = productRepo.findOneById(dto.productId);
      Sprint sprint = new Sprint()
          .setIteration(product.incrementAndGetIteration())
          .setProductId(product.getId())
          .setPlannedEnd(dto.plannedEnd);
      return sprintRepo.save(sprint).getId();
   }

   @GetMapping("sprint/{id}")
   public Sprint getSprint(@PathVariable long id) {
      return sprintRepo.findOneById(id);
   }

//   @Transactional // be mindful of this. be wary of performance (connection starvation issues)
   @PostMapping("sprint/{id}/start")
   public void startSprint(@PathVariable long id) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.start();
//      darkLogic(sprint);// do you dare to pass an attached entity into some complex logic.
      // if the Sprint entity defends its consistency correcly sprint.start();, it is SAFER to pass it in
//      Thread.sleep(1000); // common reason for reducing the scope of transaction
//      sprintMongoRepo.save(sprint);
//       sprintRepo.save(sprint); //useless IF the entity is retrieved within an open Transaction (autoflushing)
   }

   @PostMapping("sprint/{id}/end")
   public void endSprint(@PathVariable long id) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.finish();

      List<BacklogItem> notDone = sprint.getItems().stream()
          .filter(item -> item.getStatus() != BacklogItem.Status.DONE)
          .collect(Collectors.toList());

      if (notDone.size() >= 1) {
         Product product = productRepo.findOneById(sprint.getProductId());
         emailService.sendNotDoneItemsDebrief(product.getOwnerEmail(), notDone);
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
      if (sprint.getStatus() != Status.FINISHED) {
         throw new IllegalStateException();
      }
      List<BacklogItem> doneItems = sprint.getItems().stream()
          .filter(item -> item.getStatus() == BacklogItem.Status.DONE)
          .collect(Collectors.toList());
      SprintMetrics dto = new SprintMetrics();
      dto.consumedHours = sprint.getItems().stream().mapToInt(BacklogItem::getHoursConsumed).sum();
      dto.calendarDays = sprint.getStart().until(sprint.getEnd()).getDays();
      dto.doneFP = doneItems.stream().mapToInt(BacklogItem::getFpEstimation).sum();
      dto.fpVelocity = 1.0 * dto.doneFP / dto.consumedHours;
      dto.hoursConsumedForNotDone = sprint.getItems().stream()
          .filter(item -> item.getStatus() != BacklogItem.Status.DONE)
          .mapToInt(BacklogItem::getHoursConsumed).sum();
      if (sprint.getEnd().isAfter(sprint.getPlannedEnd())) {
         dto.delayDays = sprint.getPlannedEnd().until(sprint.getEnd()).getDays();
      }
      return dto;
   }

   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   static class AddBacklogItemRequest {
      public long backlogId;
      public int fpEstimation;
   }

   @PostMapping("sprint/{sprintId}/add-item")
   public Long addItem(@PathVariable long sprintId, @RequestBody AddBacklogItemRequest request) {
      BacklogItem backlogItem = backlogItemRepo.findOneById(request.backlogId);
      Sprint sprint = sprintRepo.findOneById(sprintId);
      if (sprint.getStatus() != Status.CREATED) {
         throw new IllegalStateException("Can only add items to Sprint before it starts");
      }
      backlogItem.setSprint(sprint);
      sprint.getItems().add(backlogItem);
      backlogItem.setFpEstimation(request.fpEstimation);
      return backlogItem.getId();
   }


   @PostMapping("sprint/{id}/start-item/{backlogId}")
   public void startItem(@PathVariable long id, @PathVariable long backlogId) {
      // this should be blocked
      // 1: BacklogItem.start() will be package-protected
      // 2: I will delete the BacklogItemRepo
//      backlogItemRepo.findOneById(backlogId).start();

      Sprint sprint = sprintRepo.findOneById(id);
      sprint.startItem(backlogId); // TODO after break : changing the child entity through the parent Aggregate
   }

   private final MailingListClient mailingListClient;

   @PostMapping("sprint/{id}/complete-item/{backlogId}")
   public void completeItem(@PathVariable long id, @PathVariable long backlogId) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.completeItem(backlogId);

      if (sprint.getItems().stream().allMatch(item -> item.getStatus() == BacklogItem.Status.DONE)) {
         Product product = productRepo.findOneById(sprint.getProductId());
         System.out.println("Sending CONGRATS email to team of product " + product.getCode() + ": They finished the items earlier. They have time to refactor! (OMG!)");
         List<String> emails = mailingListClient.retrieveEmails(product.getTeamMailingList());
         emailService.sendCongratsEmail(emails);
      }
   }

   @Data
   @NoArgsConstructor
   @AllArgsConstructor
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


@Entity
// AggregateRoot is responsible to enforce all contraints spanning bETWEEN the entities inside this Aggregate
class Sprint {
   @Id
   @GeneratedValue
   private Long id;
   private int iteration;

   // Reference other Aggregates via id (not via object links)
   private Long productId;

   private LocalDate start;
   private LocalDate plannedEnd;
   private LocalDate end;

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

   public void finish() {
      if (status != Status.STARTED) {
         throw new IllegalStateException();
      }
      status = Status.FINISHED;
      end = LocalDate.now();
   }

   void startItem(long backlogId) {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      backlogItemById(backlogId).start();
   }

   private BacklogItem backlogItemById(long backlogId) {
      return items.stream().filter(it -> it.getId() == backlogId).findFirst().orElseThrow();
   }

   void completeItem(long backlogId) {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      BacklogItem backlogItem = backlogItemById(backlogId);
      backlogItem.complete();
   }

   void logHours(long backlogId, int hours) {
      BacklogItem backlogItem = backlogItemById(backlogId);
      if (status != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      backlogItem.addHours(hours);
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

interface SprintRepo extends CustomJpaRepository<Sprint, Long> {
   List<Sprint> findAllByProductId(long productId);
}
