package victor.training.ddd.agile.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import victor.training.ddd.agile.event.SprintCompletedEvent;
import victor.training.ddd.agile.repo.ProductRepo;
import victor.training.ddd.agile.repo.SprintRepo;
import victor.training.ddd.agile.service.EmailService;
import victor.training.ddd.agile.service.MailingListClient;

import java.util.List;

@RequiredArgsConstructor
@Component
public class NotificationService {
  private final MailingListClient mailingListClient;
  private final EmailService emailService;
  private final SprintRepo sprintRepo;
  private final ProductRepo productRepo;

  @EventListener
  public void onSprintCompleted(SprintCompletedEvent event) {
    Sprint sprint = sprintRepo.findOneById(event.getSprintId());
    Product product = productRepo.findOneById(sprint.getProductId());
    System.out.println("Sending CONGRATS email to team of product " +
                       product.getCode() + ": They finished the items earlier." +
                       " They have time to refactor! (OMG!)");
    List<String> emails = mailingListClient.retrieveEmails(product.getTeamMailingList());
    emailService.sendCongratsEmail(emails);
  }
}
