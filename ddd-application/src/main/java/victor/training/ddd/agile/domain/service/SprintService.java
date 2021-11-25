package victor.training.ddd.agile.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.ddd.agile.*;
import victor.training.ddd.varie.Email;

import java.util.List;

import static java.util.stream.Collectors.toList;

@DDD.DomainService
@Service
@RequiredArgsConstructor
public class SprintService {
   private final BacklogItemRepo backlogItemRepo;
   private final ProductRepo productRepo;
   private final EmailService emailService;
   private final MailingListService mailingListService;

   @EventListener
   public void onSprint(BlackSprintEvent event) {
      List<BacklogItem> productBacklogItems = backlogItemRepo.findAllById(event.getNotDoneItemIds());
      List<String> notDoneTitles = productBacklogItems.stream().map(BacklogItem::getTitle).collect(toList());
      Product product = productRepo.findByCode(event.getProductCode());
      emailService.sendNotDoneItemsDebrief(product.getOwnerEmail(), notDoneTitles);
   }

   @EventListener
//   @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
   public void method(SprintFinishedEarlierEvent earlierEvent) {
      Product product = productRepo.findByCode(earlierEvent.getSprintId().productCode());
      sendEarlyFinishEmail(product);

   }

   private void sendEarlyFinishEmail(Product product) {
      String productCode = null; // sprint.getProduct().getCode()
      System.out.println("Sending CONGRATS email to team of product " + productCode +
                         ": They finished the items earlier. They have time to mob refactor! (OMG!)");
      List<Email> emails = mailingListService.retrieveEmails(product.getTeamMailingList());
      emailService.sendCongratsEmail(emails);
   }
}
