package victor.training.ddd.agile.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.domain.event.AllItemsInSpringCompletedEvent;
import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.agile.domain.model.ProductBacklogItem;
import victor.training.ddd.agile.domain.model.Sprint;
import victor.training.ddd.agile.domain.model.Sprint.Status;
import victor.training.ddd.agile.domain.model.SprintBacklogItem;
import victor.training.ddd.agile.domain.repo.ProductBacklogItemRepo;
import victor.training.ddd.agile.domain.repo.ProductRepo;
import victor.training.ddd.agile.domain.repo.SprintBacklogItemRepo;
import victor.training.ddd.agile.domain.repo.SprintRepo;
import victor.training.ddd.agile.infra.EmailService;
import victor.training.ddd.agile.infra.MailingListService;
import victor.training.ddd.agile.web.dto.AddBacklogItemRequest;
import victor.training.ddd.agile.web.dto.LogHoursRequest;
import victor.training.ddd.agile.web.dto.SprintDto;
import victor.training.ddd.agile.web.dto.SprintMetrics;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RestController
@RequiredArgsConstructor
public class SprintController {
   private final SprintRepo sprintRepo;
   private final ProductRepo productRepo;
   private final ProductBacklogItemRepo productBacklogItemRepo;
   private final SprintBacklogItemRepo sprintBacklogItemRepo;
   private final MailingListService mailingListService;
   private final EmailService emailService;


   @PostMapping("sprint")
   public Long createSprint(@RequestBody SprintDto dto) {
      Product product = productRepo.findOneById(dto.productId);
      Sprint sprint = new Sprint()
          .setIterationNumber(product.incrementAndGetIteration())
          .setProductId(product.getId())
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

      List<ProductBacklogItem> notDone = sprint.getItems().stream()
          .filter(item -> item.getStatus() != SprintBacklogItem.Status.DONE)
          .collect(Collectors.toList());

      if (notDone.size() >= 1) { // TODO Victor 2022-02-11: events instead
         Product product = productRepo.findOneById(sprint.getProductId());
         emailService.sendNotDoneItemsDebrief(product.getOwner().getEmail(), notDone);
      }
   }

   @GetMapping("sprint/{id}/metrics")
   public SprintMetrics getSprintMetrics(@PathVariable long id) {
      // TODO Victor 2022-02-11: declutter
//      Sprint sprint = sprintRepo.findOneById(id);
//      if (sprint.getStatus() != Status.FINISHED) {
//         throw new IllegalStateException();
//      }
      SprintMetrics dto = new SprintMetrics();
//      List<ProductBacklogItem> doneItems = sprint.getItems().stream()
//          .filter(item -> item.getStatus() == ProductBacklogItem.Status.DONE)
//          .collect(Collectors.toList());
//      dto.consumedHours = sprint.getItems().stream().mapToInt(ProductBacklogItem::getHoursConsumed).sum();
//      dto.calendarDays = sprint.getStart().until(sprint.getEnd()).getDays();
//      dto.doneFP = doneItems.stream().mapToInt(ProductBacklogItem::getFpEstimation).sum();
//      dto.fpVelocity = 1.0 * dto.doneFP / dto.consumedHours;
//      dto.hoursConsumedForNotDone = sprint.getItems().stream()
//          .filter(item -> item.getStatus() != ProductBacklogItem.Status.DONE)
//          .mapToInt(ProductBacklogItem::getHoursConsumed).sum();
//      if (sprint.getEnd().isAfter(sprint.getPlannedEnd())) {
//         dto.delayDays = sprint.getPlannedEnd().until(sprint.getEnd()).getDays();
//      }
      return dto;
   }
   @PostMapping("sprint/{sprintId}/add-item")
   @Transactional
   public void addItem(@PathVariable long sprintId, @RequestBody AddBacklogItemRequest request) {
      Sprint sprint = sprintRepo.findOneById(sprintId);

      sprint.addItem(request.productBacklogItemId, request.fpEstimation);

      sprintRepo.save(sprint);
   }

   @PostMapping("sprint/{sprintId}/item/{sprintBacklogItemId}/start")
   public void startItem(@PathVariable long sprintId, @PathVariable long sprintBacklogItemId) {
      ProductBacklogItem backlogItem = productBacklogItemRepo.findOneById(sprintBacklogItemId);

      Sprint sprint = sprintRepo.findOneById(sprintId);

      sprint.startItem(sprintBacklogItemId);
   }


   @PostMapping("sprint/{id}/complete-item/{sprintBacklogItemId}")
   public void completeItem(@PathVariable long id, @PathVariable long sprintBacklogItemId) {
      Sprint sprint = sprintRepo.findOneById(id);

      sprint.completeItem(sprintBacklogItemId);
   }

   @EventListener
   public void onAllItemsCompleted(AllItemsInSpringCompletedEvent event) {
      Product product = productRepo.findBySprint(event.getSprintId());

      System.out.println("Sending CONGRATS email to team of product " + product.getCode() + ": They finished the items earlier. They have time to refactor! (OMG!)");
      List<String> emails = mailingListService.retrieveEmails(product.getTeamMailingList());
      emailService.sendCongratsEmail(emails);

   }

   @PostMapping("sprint/{id}/log-hours")
   public void logHours(@PathVariable long id, @RequestBody LogHoursRequest request) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.logHours(request.sprintBacklogItemId, request.hours);
   }

}
