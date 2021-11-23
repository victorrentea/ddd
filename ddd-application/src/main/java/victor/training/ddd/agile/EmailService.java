package victor.training.ddd.agile;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailService {
   public void sendCongratsEmail(Product product) {
      System.out.println("Sending CONGRATS email to team of product " + product.getId() + ": They finished the items earlier. They have time to refactor! (OMG!)");
   }

   public void sendNotDoneItemsDebrief(List<BacklogItem> notDoneItems) {
      System.out.println("Sending Email to biz: the team was unable to declare 'DONE' the following items:\n" + notDoneItems.stream().map(BacklogItem::getTitle).collect(Collectors.joining("\n")));
      // TODO imagine external connection
   }
}
