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

@RequiredArgsConstructor
@Transactional
@RestController
public class SprintService {
   private final SprintRepo sprintRepo;
   private final ProductRepo productRepo;
   private final BacklogItemRepo backlogItemRepo;
   private final SprintMetricsGenerator sprintMetricsGenerator;

   @PostMapping("sprint")
   public Long createSprint(@RequestBody CreateSprintRequest dto) {
      Product product = productRepo.findOneById(dto.productId);
      Sprint sprint = new Sprint()
          .setIteration(product.incrementAndGetIteration())
          .setProductId(product.getId())
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
      sprint.start();
   }

   @PostMapping("sprint/{id}/end")
   public void endSprint(@PathVariable long id) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.end();
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
      return backlogItem.getId(); // Hint: if you have JPA issues getting the new ID, consider using UUID instead of sequence
   }


   @PostMapping("sprint/{sprintId}/start-item/{backlogId}")
   public void startItem(@PathVariable long sprintId, @PathVariable long backlogId) {
      sprintRepo.findOneById(sprintId).startItem(backlogId);
   }


   // facade here

   @PostMapping("sprint/{sprintId}/complete-item/{backlogId}")
   public void completeItem(@PathVariable long sprintId, @PathVariable long backlogId) {
      Sprint sprint = sprintRepo.findOneById(sprintId);
      sprint.completeItem(backlogId);
//      Product product = productRepo.findOneById(sprint.getProductId());
//      if (sprint.allItemsAreDone()) {
//         System.out.println("Sending CONGRATS email to team of product " + product.getCode() + ": They finished the items earlier. They have time to refactor! (OMG!)");
//         if (product.getTeamMailingList().isPresent()) {
//            List<String> emails = mailingListClient.retrieveEmails(product.getTeamMailingList().get());
//            emailService.sendCongratsEmail(emails);
//         }
//      }
   }

   private void checkSprintMatchesAndStarted(long sprintId, BacklogItem backlogItem) {
      if (!backlogItem.getSprint().getId().equals(sprintId)) {
         throw new IllegalArgumentException("item not in sprint");
      }
      if (backlogItem.getSprint().getStatus() != Status.STARTED) {
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
      return sprintMetricsGenerator.computeMetrics(sprint);
   }

   // PUSH in Entities:
   // - data consitency protection; eg: when state = STARTED, the startDate MUST be NOT NULL
   // - small bits of biz logic 1-3 lines IF they are highly reusable; eg. isActive() { reutrn !deleted; }

   // do not push in Entities:
   // - logic that talks to Spring @Autowired deps.
   // - logic working with 2 Entities but that doesn't belong naturally to either of them
   // - presentation logic
   // - highly specific logic
   // - complex logic (like the following)


}
