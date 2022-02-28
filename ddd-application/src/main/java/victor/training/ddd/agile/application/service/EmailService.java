package victor.training.ddd.agile.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.ddd.agile.domain.model.ProductBacklogItem;
import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.agile.domain.repo.ProductRepo;

import java.util.List;

import static java.util.stream.Collectors.joining;

@Service
@RequiredArgsConstructor
public class EmailService {
   private final EmailSender emailSender;
   private final ProductRepo productRepo;
   private final MailingListClient mailingListClient;

   public void sendCongratsEmail(Long productId) {

      Product product = productRepo.findOneById(productId);
      System.out.println("Sending CONGRATS email to team of product " + product.getCode() + ": They finished the items earlier. They have time to refactor! (OMG!)");
      List<String> emails = mailingListClient.retrieveEmails(product.getTeamMailingList());

      emailSender.sendEmail("happy@corp.intra", String.join(";", emails), "Congrats!",
          "You have finished the sprint earlier. You have more time for refactor!");
   }

   public void sendNotDoneItemsDebrief(String ownerEmail, List<ProductBacklogItem> notDoneItems) {
      String itemsStr = notDoneItems.stream().map(ProductBacklogItem::getTitle).collect(joining("\n"));
      emailSender.sendEmail("unhappy@corp.intra", ownerEmail, "Items not DONE",
          "The team was unable to declare 'DONE' the following items this iteration: " + itemsStr);
   }
}
