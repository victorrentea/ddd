package victor.training.ddd.agile;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static java.util.stream.Collectors.joining;

@Service
@RequiredArgsConstructor
public class EmailService {
   private final EmailSender emailSender;
   private final RestTemplate rest;

   public void sendCongratsEmail(Product product) {
      String[] emails = rest.getForObject("http://localhost:8989/mailing-list/" + product.getTeamMailingList(), String[].class);

      System.out.println("Sending CONGRATS email to team of product " + product.getId() + ": They finished the items earlier. They have time to refactor! (OMG!)");
      emailSender.sendEmail("happy@corp.intra", String.join(",", emails), "Congrats!",
          "You have finished the sprint earlier. You have more time for refactor!");
   }

   public void sendNotDoneItemsDebrief(String ownerEmail, List<BacklogItem> notDoneItems) {
      String itemsStr = notDoneItems.stream().map(BacklogItem::getTitle).collect(joining("\n"));
      emailSender.sendEmail("unhappy@corp.intra", ownerEmail, "Items not DONE",
          "The team was unable to declare 'DONE' the following items this iteration: " + itemsStr);
   }
}
