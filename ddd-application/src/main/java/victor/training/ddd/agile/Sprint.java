package victor.training.ddd.agile;

import lombok.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.Sprint.Status;
import victor.training.ddd.common.repo.CustomJpaRepository;
import victor.training.ddd.varie.Email;

import javax.persistence.*;
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
      if (sprint.getStatus() != Status.FINISHED) {
         throw new IllegalStateException();
      }
      SprintMetrics dto = new SprintMetrics();
      List<SprintBacklogItem> doneItems = sprint.getItems().stream()
          .filter(item -> item.getStatus() == SprintBacklogItem.Status.DONE)
          .collect(toList());
      dto.consumedHours = sprint.getItems().stream().mapToInt(SprintBacklogItem::getHoursConsumed).sum();
      dto.calendarDays = sprint.getStart().until(sprint.getEnd()).getDays();
      dto.doneFP = doneItems.stream().mapToInt(SprintBacklogItem::getFpEstimation).sum();
      dto.fpVelocity = 1.0 * dto.doneFP / dto.consumedHours;
      dto.hoursConsumedForNotDone = sprint.getItems().stream()
          .filter(item -> item.getStatus() != SprintBacklogItem.Status.DONE)
          .mapToInt(SprintBacklogItem::getHoursConsumed).sum();
      if (sprint.getEnd().isAfter(sprint.getPlannedEnd())) {
         dto.delayDays = sprint.getPlannedEnd().until(sprint.getEnd()).getDays();
      }
      return dto;
   }

   private final SprintBacklogItemRepo sprintBacklogItemRepo;
   @Data
   static class AddSprintBacklogItemRequest {
      public long backlogId;
      public int fpEstimation;
   }

   @PostMapping("sprint/{sprintId}/add-item")
   public void addItem(@PathVariable long sprintId, @RequestBody AddSprintBacklogItemRequest request) {
      SprintBacklogItem item = new SprintBacklogItem(request.backlogId, request.fpEstimation);
      Sprint sprint = sprintRepo.findOneById(sprintId);
      if (sprint.getStatus() != Status.CREATED) {
         throw new IllegalStateException("Can only add items to Sprint before it starts");
      }
      sprint.getItems().add(item);
      item.setFpEstimation(request.fpEstimation);
      sprintBacklogItemRepo.save(item);
   }


   @PostMapping("sprint/{id}/start-item/{backlogId}")
   public void startItem(@PathVariable long id, @PathVariable long backlogId) {
      SprintBacklogItem backlogItem = sprintBacklogItemRepo.findOneById(backlogId);

      // TODO studiu: am facut query direct dupa copil fara sa trec prin parinte. problema: daca ma fenteaza si modifica un SBI din alt sprint
//      if (!backlogItem.getSprint().getId().equals(id)) {
//         throw new IllegalArgumentException("item not in sprint");
//      }
      Sprint sprint = sprintRepo.findOneById(id);
      if (sprint.getStatus() != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      if (backlogItem.getStatus() != SprintBacklogItem.Status.CREATED) {
         throw new IllegalStateException("Item already started");
      }
      backlogItem.setStatus(SprintBacklogItem.Status.STARTED);
   }

   private final MailingListService mailingListService;

   @PostMapping("sprint/{id}/complete-item/{backlogId}")
   public void completeItem(@PathVariable long id, @PathVariable long backlogId) {
      SprintBacklogItem backlogItem = sprintBacklogItemRepo.findOneById(backlogId);
      checkSprintMatchesAndStarted(id, backlogItem);
      if (backlogItem.getStatus() != SprintBacklogItem.Status.STARTED) {
         throw new IllegalStateException("Cannot complete an Item before starting it");
      }
      backlogItem.setStatus(SprintBacklogItem.Status.DONE);
      Sprint sprint = sprintRepo.findOneById(id);
      if (sprint.getItems().stream().allMatch(item -> item.getStatus() == SprintBacklogItem.Status.DONE)) {
         System.out.println("Sending CONGRATS email to team of product " + sprint.getProduct().getCode() + ": They finished the items earlier. They have time to refactor! (OMG!)");
         List<Email> emails = mailingListService.retrieveEmails(sprint.getProduct().getTeamMailingList());
         emailService.sendCongratsEmail(emails);
      }
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
      SprintBacklogItem backlogItem = sprintBacklogItemRepo.findOneById(request.backlogId);
      checkSprintMatchesAndStarted(id, backlogItem);
      if (backlogItem.getStatus() != SprintBacklogItem.Status.STARTED) {
         throw new IllegalStateException("Item not started");
      }
      backlogItem.addHours(request.hours);
   }

}

@Getter
@Setter
@Entity
class SprintBacklogItem {
   @Id
   @GeneratedValue
   private Long id;
   private Long backlogItemId; // TODO add FK

   private Integer fpEstimation;
   private int hoursConsumed;

   public enum Status {
      CREATED,
      STARTED,
      DONE
   }

   @Enumerated(STRING)
   private Status status = Status.CREATED;

   public void addHours(int hours) {
      hoursConsumed += hours;
   }

   protected SprintBacklogItem() {}
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


//   private int hoursConsumateLaTerminareaSprintului;

   public enum Status {
      CREATED,
      STARTED,
      FINISHED
   }

   @Enumerated(STRING)
   private Status status = Status.CREATED;

   @OneToMany
   @JoinColumn
   private List<SprintBacklogItem> items = new ArrayList<>();

}

interface SprintRepo extends CustomJpaRepository<Sprint, Long> {
}
