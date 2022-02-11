package victor.training.ddd.agile.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.ddd.agile.domain.event.BacklogItemTitleChangedEvent;
import victor.training.ddd.agile.domain.model.BacklogItem;
import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.agile.domain.model.Release;
import victor.training.ddd.agile.domain.model.Sprint;
import victor.training.ddd.agile.domain.repo.ProductRepo;
import victor.training.ddd.agile.domain.repo.SprintRepo;

import java.util.Comparator;

import static java.util.stream.Collectors.joining;

@Transactional
@RestController
@RequiredArgsConstructor
public class ReleaseController {
   //   private final ReleaseRepo releaseRepo;
   private final ProductRepo productRepo;
   private final SprintRepo sprintRepo;

   @PostMapping("product/{productId}/release/{sprintId}")
   public Long createRelease(@PathVariable long productId, @PathVariable long sprintId) {
      Product product = productRepo.findOneById(productId);
      Sprint sprint = sprintRepo.findOneById(sprintId);


      String releasedNotes = computeReleaseNotes(sprint);

      productRepo.save(product);

      Release release = product.addRelease(sprint, releasedNotes);
      return release.getId();
   }


   //   @Async
   @EventListener
//   @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
   public void onBacklogItemTitleChanged(BacklogItemTitleChangedEvent event) {
      Long backlogItemId = event.getBacklogItemId();
      // determine relase to update
      Release release;
//      release.setReleaseNotes(computeReleaseNotes(backlogItem...sprint))
   }

   private String computeReleaseNotes(Sprint sprint) {
      Product product = sprint.getProduct();
      int fromIteration = product.getReleases().stream()
          .map(Release::getSprintId)
          .map(sprintRepo::findOneById)
          .mapToInt(Sprint::getIteration)
          .max().orElse(0);
      int toIteration = sprint.getIteration();

      String releasedNotes = product.getSprints().stream()
          .sorted(Comparator.comparing(Sprint::getIteration))
          .filter(s -> s.getIteration() >= fromIteration && s.getIteration() <= toIteration)
          .flatMap(s -> s.getItems().stream())
          .map(BacklogItem::getTitle)
          .collect(joining("\n"));
      return releasedNotes;
   }
}
