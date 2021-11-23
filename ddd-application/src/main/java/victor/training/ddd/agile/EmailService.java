package victor.training.ddd.agile;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.ddd.varie.Email;

import java.util.List;

import static java.util.stream.Collectors.joining;

@Service
@RequiredArgsConstructor
public class EmailService {
   private final EmailSender emailSender;

   public void sendCongratsEmail(List<Email> emails) {
      emailSender.sendEmail("happy@corp.intra", emails.stream().map(Email::getValue).collect(joining(";")), "Congrats!",
          "You have finished the sprint earlier. You have more time for refactor!");
   }

   public void sendNotDoneItemsDebrief(String ownerEmail, List<BacklogItem> notDoneItems) {
      String itemsStr = notDoneItems.stream().map(BacklogItem::getTitle).collect(joining("\n"));
      emailSender.sendEmail("unhappy@corp.intra", ownerEmail, "Items not DONE",
          "The team was unable to declare 'DONE' the following items this iteration: " + itemsStr);
   }
}
