package victor.training.ddd.agile.service

import org.springframework.stereotype.Service
import victor.training.ddd.agile.entity.BacklogItem
import java.util.stream.Collectors

@Service
class EmailService(private val emailSender: EmailSender) {
    fun sendCongratsEmail(emails: List<String?>?) {
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