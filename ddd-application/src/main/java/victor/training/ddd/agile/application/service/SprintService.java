package victor.training.ddd.agile.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.application.dto.AddBacklogItemRequest;
import victor.training.ddd.agile.application.dto.CreateSprintRequest;
import victor.training.ddd.agile.application.dto.LogHoursRequest;
import victor.training.ddd.agile.application.dto.SprintMetrics;
import victor.training.ddd.agile.domain.event.SprintFinishedEvent;
import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.agile.domain.model.ProductBacklogItem;
import victor.training.ddd.agile.domain.model.Sprint;
import victor.training.ddd.agile.domain.model.SprintBacklogItem;
import victor.training.ddd.agile.domain.repo.ProductBacklogItemRepo;
import victor.training.ddd.agile.domain.repo.ProductRepo;
import victor.training.ddd.agile.domain.repo.SprintRepo;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@RestController
public class SprintService {
   private final SprintRepo sprintRepo;
   private final ProductRepo productRepo;
   private final ProductBacklogItemRepo productBacklogItemRepo;
   private final EmailService emailService;
   private final SprintMetricsService sprintMetricsService;

   @PostMapping("sprint")
   public Long createSprint(@RequestBody CreateSprintRequest dto) {
      Product product = productRepo.findOneById(dto.productId);
      Sprint sprint = new Sprint(product.getId(), product.incrementAndGetIteration())
          .setPlannedEnd(dto.plannedEnd); // i imagine that some other use case might leave the sprint without a planned end.
      return sprintRepo.save(sprint).getId();
   }

   @GetMapping("sprint/{id}")
   public Sprint getSprint(@PathVariable long id) {
      return sprintRepo.findOneById(id);
   }
   // TODO move to SprintDto

   //   @Transactional // be mindful of this. be wary of performance (connection starvation issues)
   @PostMapping("sprint/{id}/start")
   @Transactional
   public void startSprint(@PathVariable long id) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.start();
   }
@Transactional
   @PostMapping("sprint/{id}/end")
   public void endSprint(@PathVariable long id) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.finish();
      sprintRepo.save(sprint); // you have to call .save on a spring Data repo. Then spring will call the
      // ,method annotated with @DomainEvents (inherited from AbstractAggregateRoot) and then publish the
      // events toall @EventListenters
   }


   // Domain Events model explicitly the side effects that propagate between Aggregates

   // processe in the same thread but different tx
   //   @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)

   // the most horror: different thread (thus, different transaction)
//   @EventListener
//   @Async

   // processed in the same thread (and tx) as the publisher
   @EventListener
   public void onSprintFinishedEvent(SprintFinishedEvent event) {
      Sprint sprint = sprintRepo.findOneById(event.getSprintId());
      List<SprintBacklogItem> notDone = sprint.getItemsNotDone();
      if (notDone.size() >= 1) {
         Product product = productRepo.findOneById(sprint.getProductId());
         List<Long> productItemIds = notDone.stream().map(SprintBacklogItem::getProductBacklogItemId).collect(toList());
         List<ProductBacklogItem> productItems = productBacklogItemRepo.findAllById(productItemIds);
         emailService.sendNotDoneItemsDebrief(product.getOwner().getEmail(), productItems);
      }
   }

   @GetMapping("sprint/{id}/metrics")
   public SprintMetrics getSprintMetrics(@PathVariable long id) {
      return sprintMetricsService.computeMetrics(id);
   }

//   @Transactional // TODO Victor 2022-03-01: Not needed anymore
   @PostMapping("sprint/{sprintId}/add-item")
   public Long addItem(@PathVariable long sprintId, @RequestBody AddBacklogItemRequest request) {
      Sprint sprint = sprintRepo.findOneById(sprintId);

      // WE PLAY A:
      // a: new SprintBacklogItem(productBacklogItem.id)

      // b: new SprintBacklogItem(productBacklogItem.title, .description)
            // + DELETE ProductBacklogItem because I do not want to duplicate title+descr OR FREEZE
            // what if the item is NOT DONE at the end of the Sprint?!!


      SprintBacklogItem sprintBacklogItem = new SprintBacklogItem(request.backlogId, request.fpEstimation);
      sprint.addItem(sprintBacklogItem);
      sprint = sprintRepo.save(sprint); // TODO Victor 2022-03-01: flush children, assign id
      return sprintBacklogItem.getId();

      // TODO Victor 2022-03-01: ID of sprintBacklogItem is not set on the item instance above
      // a) share PK with PBI
      // b) move to manually generated PF for SBI (eg UUID)
      // c) [hard] switch to 'local IDs for PBI' : composite PK (SprintId, IndexInSprint)
   }



   @Transactional
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
   @Transactional
   public void completeItem(@PathVariable long id, @PathVariable long backlogId) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.completeItem(backlogId);
      if (sprint.allItemsDone()) {
         emailService.sendCongratsEmail(sprint.getProductId());
      }
   }

   @PostMapping("sprint/{id}/log-hours")
   @Transactional
   public void logHours(@PathVariable long id, @RequestBody LogHoursRequest request) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.logHours(request.backlogId, request.hours);
   }

}
