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
import victor.training.ddd.agile.entity.SprintItem;
import victor.training.ddd.agile.repo.BacklogItemRepo;
import victor.training.ddd.agile.repo.ProductRepo;
import victor.training.ddd.agile.repo.SprintRepo;

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
        Sprint sprint = new Sprint(
                product.incrementAndGetIteration()
                , product.getId()
                , dto.plannedEnd);
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
//   @Transactional(propagation = Propagation.NOT_SUPPORTED) // TODO debate: auto-flush or no transaction?
    @PostMapping("sprint/{sprintId}/add-item")
    public String addItem(@PathVariable long sprintId, @RequestBody AddBacklogItemRequest request) {
//      Long id=sprintRepo.getNextId(); //select next val from sequence > also a nice option
        BacklogItem backlogItem = backlogItemRepo.findOneById(request.backlogId);
        Sprint sprint = sprintRepo.findOneById(sprintId);

        SprintItem sprintItem = sprint.addItem(backlogItem, request.fpEstimation);

//      sprintRepo.save(sprint);
        System.out.println("does my item have an id ? " + sprintItem.getId());
        return sprintItem.getId(); // Hint: if you have JPA issues getting the new ID, consider using UUID instead of sequence
    }


    @PostMapping("sprint/{sprintId}/start-item/{sprintItemId}")
    public void startItem(@PathVariable long sprintId, @PathVariable String sprintItemId) {
        sprintRepo.findOneById(sprintId).startItem(sprintItemId);
    }

    @PostMapping("sprint/{sprintId}/complete-item/{sprintItemId}")
    @Transactional
    public void completeItem(@PathVariable long sprintId, @PathVariable String sprintItemId) {
        Sprint sprint = sprintRepo.findOneById(sprintId);
        sprint.completeItem(sprintItemId);
//        sprintRepo.save(sprint); // WARNING: needed to publish the Domain Events from org.springframework.data.domain.AbstractAggregateRoot
//      Product product = productRepo.findOneById(sprint.getProductId());
//      if (sprint.allItemsAreDone()) {
//         System.out.println("Sending CONGRATS email to team of product " + product.getCode() + ": They finished the items earlier. They have time to refactor! (OMG!)");
//         if (product.getTeamMailingList().isPresent()) {
//            List<String> emails = mailingListClient.retrieveEmails(product.getTeamMailingList().get());
//            emailService.sendCongratsEmail(emails);
//         }
//      }
    }

    @PostMapping("sprint/{sprintId}/log-hours")
    public void logHours(@PathVariable long sprintId, @RequestBody LogHoursRequest request) {
        Sprint sprint = sprintRepo.findOneById(sprintId);
        sprint.logHours(request.sprintItemId, request.hours);
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
