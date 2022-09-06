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

import java.util.List;

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
      Product product = productRepo.findOneById(dto.getProductId());
      Sprint sprint = new Sprint()
          .setIteration(product.incrementAndGetIteration())
          .setProduct(product)
          .setPlannedEndDate(dto.getPlannedEnd());
      return sprintRepo.save(sprint).getId();
   }

   @GetMapping("sprint/{id}")
   public Sprint getSprint(@PathVariable long id) {
      return sprintRepo.findOneById(id);
   }

   @PostMapping("sprint/{id}/start")
   public void startSprint(@PathVariable long id) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.start();
      sprintRepo.save(sprint); // in your case when worlking with BL objects.
   }

   @PostMapping("sprint/{id}/end")
   public void endSprint(@PathVariable long id) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.end();
   }

   /*****************************  ITEMS IN SPRINT *******************************************/

   @PostMapping("sprint/{sprintId}/add-item")
   public Long addItem(@PathVariable long sprintId, @RequestBody AddBacklogItemRequest request) {
      BacklogItem backlogItem = backlogItemRepo.findOneById(request.getBacklogId());
      Sprint sprint = sprintRepo.findOneById(sprintId);
      if (sprint.getStatus() != Status.CREATED) {
         throw new IllegalStateException("Can only add items to Sprint before it starts");
      }
      backlogItem.setSprint(sprint);
      sprint.getItems().add(backlogItem);
      backlogItem.setFpEstimation(request.getFpEstimation());
      return backlogItem.getId(); // Hint: if you have JPA issues getting the new ID, consider using UUID instead of sequence
   }


   @PostMapping("sprint/{id}/start-item/{backlogId}")
   public void startItem(@PathVariable long id, @PathVariable long backlogId) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.startItem(backlogId);
      sprintRepo.save(sprint); // the repo within should map and save Sprint + all child BacklogItems
      // DELETE THE BacklogItemRepo !!!
   }

   private final MailingListClient mailingListClient;

   @PostMapping("sprint/{id}/complete-item/{backlogId}")
   public void completeItem(@PathVariable long id, @PathVariable long backlogId) {
      BacklogItem backlogItem = backlogItemRepo.findOneById(backlogId);

      checkSprintMatchesAndStarted(id, backlogItem);

      backlogItem.complete();


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
      BacklogItem backlogItem = backlogItemRepo.findOneById(request.getBacklogId());
      checkSprintMatchesAndStarted(id, backlogItem);
      if (backlogItem.getStatus() != BacklogItem.Status.STARTED) {
         throw new IllegalStateException("Item not started");
      }
      backlogItem.addHours(request.getHours());
   }

   @GetMapping("sprint/{id}/metrics")
   public SprintMetrics getSprintMetrics(@PathVariable long id) {
      return sprintMetricsService.getSprintMetrics(id);
   }
   private final SprintMetricsService sprintMetricsService;

}

