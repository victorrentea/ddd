package victor.training.ddd.agile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.dto.AddBacklogItemRequest;
import victor.training.ddd.agile.dto.CreateSprintRequest;
import victor.training.ddd.agile.dto.LogHoursRequest;
import victor.training.ddd.agile.entity.BacklogItem;
import victor.training.ddd.agile.entity.Product;
import victor.training.ddd.agile.entity.Sprint;
import victor.training.ddd.agile.entity.Sprint.Status;
import victor.training.ddd.agile.infra.EmailService;
import victor.training.ddd.agile.infra.MailingListClient;
import victor.training.ddd.agile.repo.BacklogItemRepo;
import victor.training.ddd.agile.repo.ProductRepo;
import victor.training.ddd.agile.repo.SprintRepo;

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
    public Sprint getSprint(@PathVariable long sprintId) {
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
        BacklogItem backlogItem = backlogItemRepo.findOneById(backlogId);
        checkSprintMatchesAndStarted(sprintId, backlogItem);

        backlogItem.complete();

        Sprint sprint = sprintRepo.findOneById(sprintId);
        if (sprint.getItems().stream().allMatch(item -> item.getStatus() == BacklogItem.Status.DONE)) {
            Product product = productRepo.findOneById(sprint.getProductId());
            System.out.println("Sending CONGRATS email to team of product " + product.getCode() + ": They finished the items earlier. They have time to refactor! (OMG!)");
            List<String> emails = mailingListClient.retrieveEmails(product.getTeamMailingList());
            emailService.sendCongratsEmail(emails);
        }
    }

    private void checkSprintMatchesAndStarted(long id, BacklogItem backlogItem) {
        // more efficient way to do: SQL/jqpl
        // SELECT 1 FROM Backlog_item bi WHERE bi.id = ?1 AND bi.sprint.id=?2 AND bi.sprint.status=?3
        // I never load the Agg in memory at all.
//        if (1!=backlogItemRepo.countByIdAndSprintIdAndSprintStatus(backlogItem.getId(), id, Status.STARTED))  {
//            throw new IllegalStateException("Sprint not started");
//        }
        // Should I do logic in SQL


        if (!backlogItem.getSprint().getId().equals(id)) {
            throw new IllegalArgumentException("item not in sprint");
        }
        Sprint sprint = sprintRepo.findOneById(id);
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


}
