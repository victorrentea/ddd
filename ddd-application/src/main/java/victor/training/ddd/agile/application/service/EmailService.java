package victor.training.ddd.agile.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.ddd.agile.domain.event.SprintFinishedEvent;
import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.agile.domain.model.ProductBacklogItem;
import victor.training.ddd.agile.domain.model.Sprint;
import victor.training.ddd.agile.domain.model.SprintBacklogItem;
import victor.training.ddd.agile.domain.repo.ProductBacklogItemRepo;
import victor.training.ddd.agile.domain.repo.ProductRepo;
import victor.training.ddd.agile.domain.repo.SprintRepo;

import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
   private final EmailSender emailSender;
   private final ProductRepo productRepo;
   private final MailingListClient mailingListClient;
   private final SprintRepo sprintRepo;
   private final ProductBacklogItemRepo productBacklogItemRepo;

   public void sendCongratsEmail(Long productId) {
      Product product = productRepo.findOneById(productId);
      if (product.getTeamMailingList().isEmpty()) {
         log.error("NO mailing list on project");
         return;
      }
      System.out.println("Sending CONGRATS email to team of product " + product.getCode() + ": They finished the items earlier. They have time to refactor! (OMG!)");
      List<String> emails = mailingListClient.retrieveEmails(product.getTeamMailingList().get());
      emailSender.sendEmail("happy@corp.intra", String.join(";", emails), "Congrats!",
          "You have finished the sprint earlier. You have more time for refactor!");
   }

   @EventListener
   public void onSprintFinishedEvent(SprintFinishedEvent event) {
      Sprint sprint = sprintRepo.findOneById(event.getSprintId());
      List<SprintBacklogItem> notDone = sprint.getItemsNotDone();
      if (notDone.isEmpty()) {
         return;
      }
      Product product = productRepo.findOneById(sprint.getProductId());
      List<Long> productItemIds = notDone.stream().map(SprintBacklogItem::getProductBacklogItemId).collect(toList());
      List<ProductBacklogItem> productItems = productBacklogItemRepo.findAllById(productItemIds);
      String itemsStr = productItems.stream().map(ProductBacklogItem::getTitle).collect(joining("\n"));
      emailSender.sendEmail("unhappy@corp.intra", product.getOwner().getEmail(), "Items not DONE",
          "The team was unable to declare 'DONE' the following items this iteration: " + itemsStr);
   }
}
