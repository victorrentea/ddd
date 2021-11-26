package victor.training.ddd.agile.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.ddd.agile.domain.service.IEmailService;
import victor.training.ddd.agile.domain.entity.Email;

import java.util.List;

import static java.util.stream.Collectors.joining;

@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {
   private final EmailSender emailSender;

   @Override
   public void sendCongratsEmail(List<Email> emails) {
      emailSender.sendEmail("happy@corp.intra", emails.stream().map(Email::getValue).collect(joining(";")), "Congrats!",
          "You have finished the sprint earlier. You have more time for refactor!");
   }

   @Override
   public void sendNotDoneItemsDebrief(String ownerEmail, List<String> notDoneItems) {
      String itemsStr = String.join(",", notDoneItems);
      emailSender.sendEmail("unhappy@corp.intra", ownerEmail, "Items not DONE",
          "The team was unable to declare 'DONE' the " +
          "following items this iteration: " + itemsStr);
   }
}
