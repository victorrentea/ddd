package victor.training.ddd.agile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.dto.AddBacklogItemRequest;
import victor.training.ddd.agile.dto.CreateSprintRequest;
import victor.training.ddd.agile.dto.LogHoursRequest;
import victor.training.ddd.agile.dto.SprintMetrics;
import victor.training.ddd.agile.entity.BacklogItem;
import victor.training.ddd.agile.entity.Product;
import victor.training.ddd.agile.entity.Sprint;
import victor.training.ddd.agile.entity.Sprint.Status;
import victor.training.ddd.agile.repo.BacklogItemRepo;
import victor.training.ddd.agile.repo.ProductRepo;
import victor.training.ddd.agile.repo.SprintRepo;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RestController
@RequiredArgsConstructor
public class SprintService {
   private final SprintRepo sprintRepo;
   private final ProductRepo productRepo;
   private final BacklogItemRepo backlogItemRepo;
   private final EmailService emailService;

   @PostMapping("sprint")
   public Long createSprint(@RequestBody CreateSprintRequest dto) {
      Product product = productRepo.findOneById(dto.productId);
      Sprint sprint = new Sprint()
          .setIteration(product.incrementAndGetIteration())
          .setProduct(product)
          .setPlannedEndDate(dto.plannedEnd);
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
      sprint.setStartDate(LocalDate.now());
      sprint.setStatus(Status.STARTED);
   }

   @PostMapping("sprint/{id}/end")
   public void endSprint(@PathVariable long id) {
      Sprint sprint = sprintRepo.findOneById(id);
      if (sprint.getStatus() != Status.STARTED) {
         throw new IllegalStateException();
      }
      sprint.setEndDate(LocalDate.now());
      sprint.setStatus(Status.FINISHED);
   }

   /*****************************  ITEMS IN SPRINT *******************************************/

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
      BacklogItem backlogItem = backlogItemRepo.findOneById(backlogId);
      checkSprintMatchesAndStarted(id, backlogItem);
      if (backlogItem.getStatus() != BacklogItem.Status.CREATED) {
         throw new IllegalStateException("Item already started");
      }
      backlogItem.setStatus(BacklogItem.Status.STARTED);
   }

   private final MailingListClient mailingListClient;

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
         System.out.println("Sending CONGRATS email to team of product " + sprint.getProduct().getCode() + ": They finished the items earlier. They have time to refactor! (OMG!)");
         List<String> emails = mailingListClient.retrieveEmails(sprint.getProduct().getTeamMailingList());
         emailService.sendCongratsEmail(emails);
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


   @PostMapping("sprint/{id}/log-hours")
   public void logHours(@PathVariable long id, @RequestBody LogHoursRequest request) {
      BacklogItem backlogItem = backlogItemRepo.findOneById(request.backlogId);
      checkSprintMatchesAndStarted(id, backlogItem);
      if (backlogItem.getStatus() != BacklogItem.Status.STARTED) {
         throw new IllegalStateException("Item not started");
      }
      backlogItem.addHours(request.hours);
   }

   /*****************************  METRICS *******************************************/

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
      dto.calendarDays = sprint.getStartDate().until(sprint.getEndDate()).getDays();
      dto.doneFP = doneItems.stream().mapToInt(BacklogItem::getFpEstimation).sum();
      dto.fpVelocity = 1.0 * dto.doneFP / dto.consumedHours;
      dto.hoursConsumedForNotDone = sprint.getItems().stream()
          .filter(item -> item.getStatus() != BacklogItem.Status.DONE)
          .mapToInt(BacklogItem::getHoursConsumed).sum();
      if (sprint.getEndDate().isAfter(sprint.getPlannedEndDate())) {
         dto.delayDays = sprint.getPlannedEndDate().until(sprint.getEndDate()).getDays();
      }
      return dto;
   }

}
