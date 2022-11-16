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
import java.util.stream.Collectors;

@Transactional
@RestController
@RequiredArgsConstructor
public class SprintService {
    private final SprintRepo sprintRepo;
    private final ProductRepo productRepo;
    private final BacklogItemRepo backlogItemRepo;
    private final EmailService emailService;
    private final MailingListClient mailingListClient;

    @PostMapping("sprint")
    public Long createSprint(@RequestBody CreateSprintRequest dto) {
        Product product = productRepo.findOneById(dto.getProductId());
        Sprint sprint = new Sprint()
                .setIteration(product.incrementAndGetIteration())
                .setProduct(product)
                .setPlannedEndDate(dto.getPlannedEnd());
        return sprintRepo.save(sprint).getId();
    }

    @GetMapping("sprint/{sprintId}")
    public Sprint getSprint(@PathVariable long sprintId) {
        return sprintRepo.findOneById(sprintId);
    }

    @PostMapping("sprint/{sprintId}/start")
    @Transactional
    public void startSprint(@PathVariable long sprintId) {
        sprintRepo.findOneById(sprintId).start();
    }

    @PostMapping("sprint/{sprintId}/end")
    public void endSprint(@PathVariable long sprintId) {
        sprintRepo.findOneById(sprintId).end();
    }

    /*****************************  ITEMS IN SPRINT *******************************************/

    @PostMapping("sprint/{sprintId}/item")
    public Long addItem(@PathVariable long sprintId, @RequestBody AddBacklogItemRequest request) {
        BacklogItem backlogItem = backlogItemRepo.findOneById(request.getBacklogId());
        Sprint sprint = sprintRepo.findOneById(sprintId);
        if (sprint.getStatus() != Status.CREATED) {
            throw new IllegalStateException("Can only add items to Sprint before it starts");
        }

        // risk of bidirectional JPA link ? WHY SHOULD YOU AVOID IT IN GENERAL (not only in DDD)
        // - cycles at serialization
        // - who is the owner - not clear?
        // - tech problem: you forget to set one of the sides

//        backlogItem.setSprint(sprint); // PERSISTS THE FK in DB
//        sprint.getItems().add(backlogItem); // keeps the Object model in sync.
        // if you really insist on using bidirectional link
        sprint.addItem(backlogItem);

        backlogItem.setFpEstimation(request.getFpEstimation());
        return backlogItem.getId(); // Hint: if you have JPA issues getting the new ID, consider using UUID instead of sequence
    }

    @PostMapping("sprint/{sprintId}/item/{backlogId}/start")
    public void startItem(@PathVariable long sprintId, @PathVariable long backlogId) {
        sprintRepo.findOneById(sprintId).startItem(backlogId/*, backlogItemRepo*/);

//        BacklogItem backlogItem = backlogItemRepo.findOneById(backlogId);
//        checkSprintMatchesAndStarted(sprintId, backlogItem);
//
//        backlogItem.start();
    }

    @PostMapping("sprint/{sprintId}/item/{backlogId}/complete")
    public void completeItem(@PathVariable long sprintId, @PathVariable long backlogId) {
        sprintRepo.findOneById(sprintId).completeItem(backlogId);

        Sprint sprint = sprintRepo.findOneById(sprintId);
        if (sprint.getItems().stream().allMatch(item -> item.getStatus() == BacklogItem.Status.DONE)) {
            System.out.println("Sending CONGRATS email to team of product " + sprint.getProduct().getCode() + ": They finished the items earlier. They have time to refactor! (OMG!)");
            List<String> emails = mailingListClient.retrieveEmails(sprint.getProduct().getTeamMailingList());
            emailService.sendCongratsEmail(emails);
        }
    }

    private void checkSprintMatchesAndStarted(long sprintId, BacklogItem backlogItem) {
        if (!backlogItem.getSprint().getId().equals(sprintId)) {
            throw new IllegalArgumentException("item not in sprint");
        }
        Sprint sprint = sprintRepo.findOneById(sprintId);
        if (sprint.getStatus() != Status.STARTED) {
            throw new IllegalStateException("Sprint not started");
        }
    }


    @PostMapping("sprint/{sprintId}/log-hours")
    public void logHours(@PathVariable long sprintId, @RequestBody LogHoursRequest request) {
        BacklogItem backlogItem = backlogItemRepo.findOneById(request.getBacklogId());
        checkSprintMatchesAndStarted(sprintId, backlogItem);
        if (backlogItem.getStatus() != BacklogItem.Status.STARTED) {
            throw new IllegalStateException("Item not started");
        }
        backlogItem.addHours(request.getHours());
    }

    /*****************************  METRICS *******************************************/

    @GetMapping("sprint/{sprintId}/metrics")
    public SprintMetrics getSprintMetrics(@PathVariable long sprintId) {
        Sprint sprint = sprintRepo.findOneById(sprintId);
        if (sprint.getStatus() != Status.FINISHED) {
            throw new IllegalStateException();
        }
        List<BacklogItem> doneItems = sprint.getItems().stream()
                .filter(item -> item.getStatus() == BacklogItem.Status.DONE)
                .collect(Collectors.toList());
        SprintMetrics dto = new SprintMetrics();
        dto.setConsumedHours(sprint.getItems().stream().mapToInt(BacklogItem::getHoursConsumed).sum());
        dto.setCalendarDays(sprint.getStartDate().until(sprint.getEndDate()).getDays());
        dto.setDoneFP(doneItems.stream().mapToInt(BacklogItem::getFpEstimation).sum());
        dto.setFpVelocity(1.0 * dto.getDoneFP() / dto.getConsumedHours());
        dto.setHoursConsumedForNotDone(sprint.getItems().stream()
                .filter(item -> item.getStatus() != BacklogItem.Status.DONE)
                .mapToInt(BacklogItem::getHoursConsumed).sum());
        if (sprint.getEndDate().isAfter(sprint.getPlannedEndDate())) {
            dto.setDelayDays(sprint.getPlannedEndDate().until(sprint.getEndDate()).getDays());
        }
        return dto;
    }

}
