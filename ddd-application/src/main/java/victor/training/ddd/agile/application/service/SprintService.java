package victor.training.ddd.agile.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.application.dto.AddBacklogItemRequest;
import victor.training.ddd.agile.application.dto.CreateSprintRequest;
import victor.training.ddd.agile.application.dto.LogHoursRequest;
import victor.training.ddd.agile.application.dto.SprintMetrics;
import victor.training.ddd.agile.domain.event.ItemAddedEvent;
import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.agile.domain.model.Sprint;
import victor.training.ddd.agile.domain.model.SprintBacklogItem;
import victor.training.ddd.agile.domain.repo.ProductBacklogItemRepo;
import victor.training.ddd.agile.domain.repo.ProductRepo;
import victor.training.ddd.agile.domain.repo.SprintRepo;

@Slf4j
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
   // TODO move to SprintDto
   public Sprint getSprint(@PathVariable long id) {
      return sprintRepo.findOneById(id);
   }

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

   @GetMapping("sprint/{id}/metrics")
   public SprintMetrics getSprintMetrics(@PathVariable long id) {
      return sprintMetricsService.computeMetrics(id);
   }

   @Transactional
   @PostMapping("sprint/{sprintId}/add-item")
   public String addItem(@PathVariable long sprintId, @RequestBody AddBacklogItemRequest request) {
      Sprint sprint = sprintRepo.findOneById(sprintId);
      SprintBacklogItem sprintBacklogItem = new SprintBacklogItem(request.productBacklogId, request.fpEstimation);
      sprint.addItem(sprintBacklogItem);
      return sprintBacklogItem.getId();
      //IDs
      // a) manually assign ID > give up @GeneratedValue
      // b) UUID: move to manually generated PK for SBI (eg UUID) > BRUTAL because I had to move from Long to String
             // not DEV friendly> hack short-UUID in pre prod envs.
      // c) SprintBacklogItem to share the PK with ProductBacklogItem < DROP because there could be 2 SBI linked to the same 1 PBI
      // d) [hard] switch to 'local IDs for PBI' : SBI to have a composite PK (SprintId, IndexInSprint)
   }

   @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
   public void afterItemAddedTxCommited(ItemAddedEvent event) {
      log.debug("After tx sending to kafka " + event.getSprintBacklogItemId());
//      mq.send(sprintBacklogItem.getId());

   }


   @Transactional
   @PostMapping("sprint/{id}/start-item/{backlogId}")
   public void startItem(@PathVariable long id, @PathVariable String backlogId) {
      // this should be blocked
      // 1: BacklogItem.start() will be package-protected
      // 2: I will delete the BacklogItemRepo
//      backlogItemRepo.findOneById(backlogId).start();

      Sprint sprint = sprintRepo.findOneById(id);
      sprint.startItem(backlogId); // TODO after break : changing the child entity through the parent Aggregate
   }

   @PostMapping("sprint/{id}/complete-item/{backlogId}")
   public void completeItem(@PathVariable long id, @PathVariable String backlogId) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.completeItem(backlogId);
      sprintRepo.save(sprint);
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
