package victor.training.ddd.agile;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.Sprint.Status;
import victor.training.ddd.common.repo.CustomJpaRepository;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
      Sprint sprint = new Sprint()
          .setIteration(product.incrementAndGetIteration())
          .setProduct(product)
          .setPlannedEnd(dto.plannedEnd);
      return sprintRepo.save(sprint).getId();
   }

   @GetMapping("sprint/{id}")
   public Sprint getSprint(@PathVariable long id) {
      return sprintRepo.findOneById(id);
   }

   @PostMapping("sprint/{id}/start")
   public void startSprint(@PathVariable long id) {
      Sprint sprint = sprintRepo.findOneById(id);
      if (sprint.getStatus() != Status.CREATED) {
         throw new IllegalStateException();
      }
      sprint.setStart(LocalDate.now());
      sprint.setStatus(Status.STARTED);
   }

   @PostMapping("sprint/{id}/end")
   public void endSprint(@PathVariable long id) {
      Sprint sprint = sprintRepo.findOneById(id);
      if (sprint.getStatus() != Status.STARTED) {
         throw new IllegalStateException();
      }
      sprint.setEnd(LocalDate.now());
      sprint.setStatus(Status.FINISHED);

      List<BacklogItem> notDone = sprint.getItems().stream()
          .filter(item -> item.getStatus() != BacklogItem.Status.DONE)
          .collect(Collectors.toList());

      if (notDone.size() >= 1) {
         emailService.sendNotDoneItemsDebrief(sprint.getProduct().getOwner().getEmail(), notDone);
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
      SprintMetrics dto = new SprintMetrics();
      List<BacklogItem> doneItems = sprint.getItems().stream()
          .filter(item -> item.getStatus() == BacklogItem.Status.DONE)
          .collect(Collectors.toList());
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
   static class AddBacklogItemRequest {
      public long backlogId;
      public int fpEstimation;
   }

   @PostMapping("sprint/{sprintId}/add-item")
   @Transactional
   public void addItem(@PathVariable long sprintId, @RequestBody AddBacklogItemRequest request) {
      BacklogItem backlogItem = backlogItemRepo.findOneById(request.backlogId);

      Sprint sprint = sprintRepo.findOneById(sprintId);

      sprint.addItem(backlogItem, request.fpEstimation);
   }


   @PostMapping("sprint/{sprintId}/item/{backlogId}/start")
   public void startItem(@PathVariable long sprintId, @PathVariable long backlogId) {
      BacklogItem backlogItem = backlogItemRepo.findOneById(backlogId);

      Sprint sprint = sprintRepo.findOneById(sprintId);

      sprint.checkSprintMatchesAndStarted(backlogItem);

      backlogItem.start();
   }








   private final MailingListService mailingListService;

   @PostMapping("sprint/{id}/complete-item/{backlogId}")
   public void completeItem(@PathVariable long id, @PathVariable long backlogId) {
      BacklogItem backlogItem = backlogItemRepo.findOneById(backlogId);
      Sprint sprint = sprintRepo.findOneById(id);

      sprint.checkSprintMatchesAndStarted(backlogItem);

      backlogItem.complete();

      if (sprint.getItems().stream().allMatch(item -> item.getStatus() == BacklogItem.Status.DONE)) {
         System.out.println("Sending CONGRATS email to team of product " + sprint.getProduct().getCode() + ": They finished the items earlier. They have time to refactor! (OMG!)");
         List<String> emails = mailingListService.retrieveEmails(sprint.getProduct().getTeamMailingList());
         emailService.sendCongratsEmail(emails);
      }
   }

   @Data
   static class LogHoursRequest {
      public long backlogId;
      public int hours;
   }

   @PostMapping("sprint/{id}/log-hours")
   public void logHours(@PathVariable long id, @RequestBody LogHoursRequest request) {
      BacklogItem backlogItem = backlogItemRepo.findOneById(request.backlogId);
      Sprint sprint = sprintRepo.findOneById(id);

      sprint.checkSprintMatchesAndStarted(backlogItem);
      if (backlogItem.getStatus() != BacklogItem.Status.STARTED) {
         throw new IllegalStateException("Item not started");
      }
      backlogItem.addHours(request.hours);
   }

}


@Entity
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

   public Sprint() {
   }

   public void checkSprintMatchesAndStarted(BacklogItem backlogItem) {
      if (!backlogItem.getSprint().getId().equals(getId())) {
         throw new IllegalArgumentException("item not in sprint");
      }
      if (getStatus() != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
   }

   public Long getId() {
      return this.id;
   }

   public int getIteration() {
      return this.iteration;
   }

   public Product getProduct() {
      return this.product;
   }

   public LocalDate getStart() {
      return this.start;
   }

   public LocalDate getPlannedEnd() {
      return this.plannedEnd;
   }

   public LocalDate getEnd() {
      return this.end;
   }

   public Status getStatus() {
      return this.status;
   }

   public List<BacklogItem> getItems() {
      return Collections.unmodifiableList(this.items);
   }

   public Sprint setId(Long id) {
      this.id = id;
      return this;
   }

   public Sprint setIteration(int iteration) {
      this.iteration = iteration;
      return this;
   }

   public Sprint setProduct(Product product) {
      this.product = product;
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

   public void addItem(BacklogItem backlogItem, int fpEstimation) {
      if (status != Status.CREATED) {
         throw new IllegalStateException("Can only add items to Sprint before it starts");
      }
      backlogItem.setFpEstimation(fpEstimation);
      items.add(backlogItem);
      backlogItem.setSprint(this); // you will see the setter from here
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
}
