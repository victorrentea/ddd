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
   private final ProductRepo productRepo;
   private final SprintRepo sprintRepo;

   @PostMapping("product/{productId}/release/{sprintId}")
   public Long createRelease(@PathVariable long productId, @PathVariable long sprintId) {
      Product product = productRepo.findOneById(productId);
      Sprint sprint = sprintRepo.findOneById(sprintId);

      String releaseNotes = computeReleaseNotes(sprint); // TODO vrentea 2022-02-11 encapsulate

      productRepo.save(product); // TODO vrentea 2022-02-11 TWO

      Release release = product.addRelease(sprint, releaseNotes);
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

   private String computeReleaseNotes(Sprint sprint) {
      Product product = sprint.getProduct();
      int fromIterationNumber = product.getReleases().stream()
          .map(Release::getSprintId)
          .map(sprintRepo::findOneById)
          .mapToInt(Sprint::getIteration)
          .max().orElse(0);
      int toIterationNumber = sprint.getIteration();

      return product.getSprints().stream()
          .sorted(Comparator.comparing(Sprint::getIteration))
          .filter(s -> s.getIteration() >= fromIterationNumber && s.getIteration() <= toIterationNumber)
          .flatMap(s -> s.getItems().stream())
          .map(BacklogItem::getTitle)
          .collect(joining("\n"));
   }
}
