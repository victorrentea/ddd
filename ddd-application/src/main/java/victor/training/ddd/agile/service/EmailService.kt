package victor.training.ddd.agile.service

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import victor.training.ddd.agile.entity.BacklogItem
import victor.training.ddd.agile.entity.SprintItemsFinishedEvent
import victor.training.ddd.agile.repo.SprintRepo
import java.util.stream.Collectors

@Service
class EmailService(
    private val emailSender: EmailSender,
    private val mailingListClient: MailingListClient,
    private val sprintRepo: SprintRepo
    ) {

    private val log = LoggerFactory.getLogger(EmailService::class.java)

    // FatEvent + exception after > listener works on a copy , but what if the tx gets rolled back > the listener running still works on a CHANGED COPY of the entity.

//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onSprintFinished(event: SprintItemsFinishedEvent) {
    println("Would the following line cause a SELECT?")
        val sprint = sprintRepo.findOneById(event.sprintId) //
    println("Lets see")
        sendCongratsEmail(sprint.product.code, sprint.product.teamMailingList)
    }

    fun sendCongratsEmail(productCode: String, teamMailingList: String) {
        log.debug("Sending CONGRATS email to team of product " + productCode + ": You finished the sprint. They have time to refactor! (OMG!)")

        log.debug("in the same tx: " + sprintRepo.count())


        val emails = mailingListClient.retrieveEmails(teamMailingList)
        emailSender.sendEmail(
            "happy@corp.intra",
            java.lang.String.join(";", emails),
            "Congrats!",
            "You have finished the sprint earlier. You have more time for refactor!"
        )
    }

    fun sendNotDoneItemsDebrief(ownerEmail: String?, notDoneItems: List<BacklogItem>) {
        val itemsStr = notDoneItems.stream().map { obj: BacklogItem -> obj.title }.collect(Collectors.joining("\n"))
        emailSender.sendEmail(
            "unhappy@corp.intra",
            ownerEmail,
            "Items not DONE",
            "The team was unable to declare 'DONE' the following items this iteration: $itemsStr"
        )
    }
}