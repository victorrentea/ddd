package victor.training.ddd.agile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
class SprintController {
   private final SprintRepo sprintRepo;
   private final ProductRepo productRepo;
   private final BacklogItemRepo backlogItemRepo;
   private final EmailService emailService;

   static class SprintDto {
      public Long productId;
      public LocalDate plannedEnd;
   }

   @PostMapping("sprint")
   public void createSprint(@RequestBody SprintDto dto) {
      Product product = productRepo.findOneById(dto.productId);
      Sprint sprint = new Sprint()
          .setIteration(product.incrementAndGetIteration())
          .setProduct(product)
          .setPlannedEnd(dto.plannedEnd);
      sprintRepo.save(sprint);
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
         emailService.sendNotDoneItemsDebrief(sprint.getProduct().getOwnerEmail(), notDone);
      }
   }

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

   static class AddBacklogItemRequest {
      public long backlogId;
      public int fpEstimation;
   }

   @PostMapping("sprint/{id}/add-item")
   public void addItem(@PathVariable long id, @RequestBody AddBacklogItemRequest request) {
      BacklogItem backlogItem = backlogItemRepo.findOneById(request.backlogId);
      Sprint sprint = sprintRepo.findOneById(id);
      if (sprint.getStatus() != Status.CREATED) {
         throw new IllegalStateException("Can only add items to Sprint before it starts");
      }
      backlogItem.setSprint(sprint);
      backlogItem.setFpEstimation(request.fpEstimation);
   }


   @PostMapping("sprint/{id}/start-item/{backlogId}")
   public void startItem(@PathVariable long id, @PathVariable long backlogId) {
      BacklogItem backlogItem = backlogItemRepo.findOneById(backlogId);
      checkSprintMatchesAndStarted(id, backlogItem);
      if (backlogItem.getStatus() != BacklogItem.Status.CREATED) {
         throw new IllegalStateException("Item already started");
      }
      backlogItem.setStatus(BacklogItem.Status.STARTED);
   }

   @PostMapping("sprint/{id}/complete-item/{backlogId}")
   public void completeItem(@PathVariable long id, @PathVariable long backlogId) {
      BacklogItem backlogItem = backlogItemRepo.findOneById(backlogId);
      checkSprintMatchesAndStarted(id, backlogItem);
      if (backlogItem.getStatus() != BacklogItem.Status.STARTED) {
         throw new IllegalStateException("Cannot complete an Item before starting it");
      }
      backlogItem.setStatus(BacklogItem.Status.DONE);
      Sprint sprint = sprintRepo.findOneById(id);
      if (sprint.getItems().stream().allMatch(item -> item.getStatus() == BacklogItem.Status.DONE)) {
         emailService.sendCongratsEmail(sprint.getProduct());
      }
   }

   private void checkSprintMatchesAndStarted(long id, BacklogItem backlogItem) {
      if (!backlogItem.getSprint().getId().equals(id)) {
         throw new IllegalArgumentException("item not in sprint");
      }
      Sprint sprint = sprintRepo.findOneById(id);
      if (sprint.getStatus() != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
   }

   static class LogHoursRequest {
      public long backlogId;
      public int hours;
   }

   @PostMapping("sprint/{id}/log-hours")
   public void logHours(@PathVariable long id, @RequestBody LogHoursRequest request) {
      BacklogItem backlogItem = backlogItemRepo.findOneById(request.backlogId);
      checkSprintMatchesAndStarted(id, backlogItem);
      if (backlogItem.getStatus() != BacklogItem.Status.STARTED) {
         throw new IllegalStateException("Item not started");
      }
      backlogItem.addHours(request.hours);
   }

}


@Getter
@Setter
@NoArgsConstructor
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

   public enum Status {
      CREATED,
      STARTED,
      FINISHED
   }

   @Enumerated(STRING)
   private Status status;

   @OneToMany(mappedBy = "sprint")
   private List<BacklogItem> items = new ArrayList<>();

}

interface SprintRepo extends CustomJpaRepository<Sprint, Long> {
}
