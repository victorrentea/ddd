package victor.training.ddd.agile.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.application.dto.AddBacklogItemRequest;
import victor.training.ddd.agile.application.dto.CreateSprintRequest;
import victor.training.ddd.agile.application.dto.LogHoursRequest;
import victor.training.ddd.agile.domain.events.SprintCompletedEvent;
import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.agile.domain.model.Sprint;
import victor.training.ddd.agile.domain.model.Sprint.Status;
import victor.training.ddd.agile.domain.model.SprintItem;
import victor.training.ddd.agile.domain.repo.BacklogItemRepo;
import victor.training.ddd.agile.domain.repo.ProductRepo;
import victor.training.ddd.agile.domain.repo.SprintItemRepo;
import victor.training.ddd.agile.domain.repo.SprintRepo;
import victor.training.ddd.agile.infra.EmailService;
import victor.training.ddd.agile.infra.MailingListClient;

import java.util.List;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Slf4j
@Transactional // TODO only use it where NEEDED:
@RestController
@RequiredArgsConstructor
public class SprintApplicationService implements SprintApplicationServiceApi {
    private final SprintRepo sprintRepo;
    private final ProductRepo productRepo;
    private final BacklogItemRepo backlogItemRepo;
    private final EmailService emailService;
    private final MailingListClient mailingListClient;

    @Override
    public Long createSprint(@RequestBody CreateSprintRequest dto) {
        Product product = productRepo.findOneById(dto.getProductId());
        Sprint sprint = new Sprint(product.incrementAndGetIteration(), product.getId())
                .setPlannedEndDate(dto.getPlannedEnd());
        return sprintRepo.save(sprint).getId();
    }

    @Override
    @GetMapping("sprint/{sprintId}")
    public Sprint getSprint( long sprintId) { // TODO expose a DTO instead
        return sprintRepo.findOneById(sprintId);
    }

    @Override
    public void startSprint( long sprintId) {
        sprintRepo.findOneById(sprintId).start();
    }

    @Override
    public void endSprint( long sprintId) {
        sprintRepo.findOneById(sprintId).end();
    }

    /*****************************  ITEMS IN SPRINT *******************************************/
    private final SprintItemRepo sprintItemRepo;

    @Override
    public Long addItem( long sprintId, @RequestBody AddBacklogItemRequest request) {
        Sprint sprint = sprintRepo.findOneById(sprintId);

        Long siId = sprintItemRepo.newId();
        SprintItem sprintItem = new SprintItem(
                siId, // <- is this better that @GeneratedValue ?
                request.getBacklogId(),
                request.getFpEstimation());

//        sprintItemRepo.save(sprintItem);
//       Long siId = sprintItem.getId()// set by hibernate (without sequences)

        // When is it useful to have the ID before the end of TX ?
        //

        sprint.addItem(sprintItem);
        return siId; // Hint: if you have JPA issues getting the new ID, consider using UUID instead of sequence
    }


    @Override
    public void startItem( long sprintId,  long springItemId) {
        Sprint sprint = sprintRepo.findOneById(sprintId);
        sprint.startItem(springItemId);
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


    //        @PutMapping("sprint/{sprintId}/item/{springItemId}/status")// Marko ❤️ this (REST)
    //        public void setStatus (){
    //            if (start) {
    //                startItem(sprintId, springItemId);
    //            } else {
    //                completeItem(sprintId, springItemId);
    //            }
    //        }


    @Override
    public void completeItem( long sprintId,  long springItemId) {
        Sprint sprint = sprintRepo.findOneById(sprintId);
        sprint.completeItem(springItemId);
        sprintRepo.save(sprint); // fires the @PreUpdate hibernate event -> spring -> AbstractAggregateRoot.domainEvents
        // easier to tst : you just have to look in the agg.domainEvents list.




        // WHF happens?!!!

        // When an aggregate changes need to trigger other changes in other parts (email sent, fields in other agg)
        // MORE LOGIC INSIDE THE MODEL !!

        // 1) orchestration from a A/D service

        // 2) pass a DS/Repo/I as arg to the agg method sprint.completeItem(springItemId, notificationService);
        // 2bis) sprint.completeItem(springItemId, -> );
        // - not very scalable 1-2;
        // - weird; still couples AS/DS with that NotificaitonService
        // - what if the callback throws exception? I would like the complete() still to happen,
        // despite exception in the (nice-to-have) notificaton/audit/BI;
        //  events fix: -> @TransactionEventListene(phase=AFTER_COMMIT), @Async @EventListener

        // 3) Events from Aggregate

        // 4) store the entity. @Scheduled (rate=1000) select completed sprint
        // SELECT FOR UPDATE * from SPRINT where sent_email=0
        // send
        // UPDATE SPRINT WHERE id =? sent_email=1 > you can still send duplicated emails

        // 5) the final solution : Debezium / CDC Emits ONE!!
        // Kafka message automatically by tailing the transaction log

        // how can I make sure that whenever I complete an Item, the email gets sent ?!!!!
        //            if (sprint.allItemsDone()) {
        //                Product product = productRepo.findOneById(sprint.getProductId());
        //                System.out.println("Sending CONGRATS email to team of product " + product.getCode() + ": They finished the items earlier. They have time to refactor! (OMG!)");
        //                List<String> emails = mailingListClient.retrieveEmails(product.getTeamMailingList());
        //                // this is NOT the core domain biz of Sprint
        //                emailService.sendCongratsEmail(emails);
        //            }
    }

    //    @Order(2)
    @Override
    @EventListener
    public void meeeeToo(SprintCompletedEvent event) {
        // who goes first ?
    }

    @EventListener
    // handling: sync in tx, sync after tx, async (🙏)
    //    @Async // fire-and-forget : not important side-effects
    //    @TransactionalEventListener(phase = AFTER_COMMIT) // only if COMMIT was OK
    public void overTheHillsAndFarAway(SprintCompletedEvent event) {
        Sprint sprint = sprintRepo.findOneById(event.getSprintId());
        Product product = productRepo.findOneById(sprint.getProductId());
        SprintApplicationService.log.info("Sending CONGRATS email to team of product " + product.getCode() + ": They finished the items earlier. They have time to refactor! (OMG!)");
        List<String> emails = mailingListClient.retrieveEmails(product.getTeamMailingList());
        // this is NOT the core domain biz of Sprint
        emailService.sendCongratsEmail(emails);
    }



    @Override
    public void logHours( long sprintId, @RequestBody LogHoursRequest request) {
        Sprint sprint = sprintRepo.findOneById(sprintId);
        sprint.logHoursForItem(request.getBacklogId(), request.getHours());
    }

}
