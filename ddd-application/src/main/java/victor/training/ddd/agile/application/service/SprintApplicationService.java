package victor.training.ddd.agile.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.application.dto.AddBacklogItemRequest;
import victor.training.ddd.agile.application.dto.CreateSprintRequest;
import victor.training.ddd.agile.application.dto.LogHoursRequest;
import victor.training.ddd.agile.domain.model.BacklogItem;
import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.agile.domain.model.Sprint;
import victor.training.ddd.agile.domain.model.Sprint.Status;
import victor.training.ddd.agile.infra.EmailService;
import victor.training.ddd.agile.infra.MailingListClient;
import victor.training.ddd.agile.domain.repo.BacklogItemRepo;
import victor.training.ddd.agile.domain.repo.ProductRepo;
import victor.training.ddd.agile.domain.repo.SprintRepo;

import java.time.LocalDate;
import java.util.List;

@Transactional
@RestController
@RequiredArgsConstructor
public class SprintApplicationService {
    private final SprintRepo sprintRepo;
    private final ProductRepo productRepo;
    private final BacklogItemRepo backlogItemRepo;
    private final EmailService emailService;
    private final MailingListClient mailingListClient;

    @PostMapping("sprint")
    public Long createSprint(@RequestBody CreateSprintRequest dto) {
        Product product = productRepo.findOneById(dto.getProductId());
        Sprint sprint = new Sprint(product.incrementAndGetIteration(), product.getId())
                .setPlannedEndDate(dto.getPlannedEnd());
        return sprintRepo.save(sprint).getId();
    }

    @GetMapping("sprint/{sprintId}")
    public Sprint getSprint(@PathVariable long sprintId) { // TODO expose a DTO instead
        return sprintRepo.findOneById(sprintId);
    }

    @PostMapping("sprint/{sprintId}/start")
    public void startSprint(@PathVariable long sprintId) {
        Sprint sprint = sprintRepo.findOneById(sprintId);
        if (sprint.getStatus() != Status.CREATED) {
            throw new IllegalStateException();
        }
        sprint.setStartDate(LocalDate.now());
        sprint.setStatus(Status.STARTED);
    }

    @PostMapping("sprint/{sprintId}/end")
    public void endSprint(@PathVariable long sprintId) {
        Sprint sprint = sprintRepo.findOneById(sprintId);
        if (sprint.getStatus() != Status.STARTED) {
            throw new IllegalStateException();
        }
        sprint.setEndDate(LocalDate.now());
        sprint.setStatus(Status.FINISHED);
    }

    /*****************************  ITEMS IN SPRINT *******************************************/

    @PostMapping("sprint/{sprintId}/item")
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

    @PostMapping("sprint/{sprintId}/item/{backlogId}/start")
    public void startItem(@PathVariable long sprintId, @PathVariable long backlogId) {
        Sprint sprint = sprintRepo.findOneById(sprintId);
        sprint.startItem(backlogId);
        // the magic of the Repository pattern:
        // the illusion of an in-memory store.
        // there is no SAVE 😨: ORM auto-flushes the dirty entities (Item.status)

        // option1: rely on magic and have everyone understand it
            // risk: anInnocentMethod(sprint); //

        // option2: reject/ban the magic, burn the witches . Dark Ages *inquision*
//        sprintRepo.save(sprint); // + delete the @Transactional on the top
        //#1 + #2 : if you change just the name, hibernate will still update the entire row (all the columns)

        // option3: flag hibernate to track down individual fields

        // option4: dedicated: = NO HIBERNATE : JdbcTemplate or JPQL @Modifying
        // sprintRepo.updateName(sprintId, "newName");

    }

    @PostMapping("sprint/{sprintId}/item/{backlogId}/complete")
    public void completeItem(@PathVariable long sprintId, @PathVariable long backlogId) {
        Sprint sprint = sprintRepo.findOneById(sprintId);
        sprint.completeItem(backlogId);

        if (sprint.getItems().stream().allMatch(item -> item.getStatus() == BacklogItem.Status.DONE)) {
            Product product = productRepo.findOneById(sprint.getProductId());
            System.out.println("Sending CONGRATS email to team of product " + product.getCode() + ": They finished the items earlier. They have time to refactor! (OMG!)");
            List<String> emails = mailingListClient.retrieveEmails(product.getTeamMailingList());
            emailService.sendCongratsEmail(emails);
        }
    }

    @PostMapping("sprint/{sprintId}/log-hours")
    public void logHours(@PathVariable long sprintId, @RequestBody LogHoursRequest request) {
        Sprint sprint = sprintRepo.findOneById(sprintId);
        sprint.logHoursForItem(request.getBacklogId(), request.getHours());
    }

}
