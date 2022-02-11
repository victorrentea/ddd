package victor.training.ddd.agile.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
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

@RestController
@RequiredArgsConstructor
public class ReleaseController {
   private final ProductRepo productRepo;
   private final SprintRepo sprintRepo;

   @PostMapping("product/{productId}/release/{sprintId}")
   public Long createRelease(@PathVariable long productId, @PathVariable long sprintId) {
      Product product = productRepo.findOneById(productId);
      Sprint sprint = sprintRepo.findOneById(sprintId);

      String releaseNotes = computeReleaseNotes(sprint); // TODO vrentea 2022-02-11 encapsulate

      Release release = product.addRelease(sprint, releaseNotes);

      productRepo.save(product);
      return release.getId();
   }


   //   @Async
   @EventListener // TODO Victor 2022-02-11: implement (at end)
   public void onBacklogItemTitleChanged(BacklogItemTitleChangedEvent event) {
      Long backlogItemId = event.getBacklogItemId();
      // determine relase to update
      Release release;
//      release.setReleaseNotes(computeReleaseNotes(backlogItem...sprint))
   }


   // we want to collect all backlog item titles from all sprints since the last release.
   private String computeReleaseNotes(Sprint sprint) {
      Product product = sprint.getProduct();

      int fromIterationNumber = product.getReleases().stream()
          .mapToInt(Release::getIterationNumber)
          .max()
          .orElse(0);
      int toIterationNumber = sprint.getIterationNumber();

      return product.getSprints().stream()
          .sorted(Comparator.comparing(Sprint::getIterationNumber))
          .filter(s -> s.getIterationNumber() >= fromIterationNumber && s.getIterationNumber() <= toIterationNumber)
          .flatMap(s -> s.getItems().stream())
          .map(BacklogItem::getTitle)
          .collect(joining("\n"));
   }
}
