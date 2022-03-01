package victor.training.ddd.agile.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.ddd.agile.domain.model.*;
import victor.training.ddd.agile.domain.repo.ProductBacklogItemRepo;
import victor.training.ddd.agile.domain.repo.ProductRepo;
import victor.training.ddd.agile.domain.repo.ReleaseRepo;
import victor.training.ddd.agile.domain.repo.SprintRepo;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Transactional
@RestController
@RequiredArgsConstructor
public class ReleaseService {
   private final ReleaseRepo releaseRepo;
   private final ProductRepo productRepo;
   private final SprintRepo sprintRepo;
   private final ProductBacklogItemRepo productBacklogItemRepo;

   @PostMapping("product/{productId}/release/{sprintId}")
   public Release createRelease(@PathVariable long productId, @PathVariable long sprintId) {
      Product product = productRepo.findOneById(productId);
      Sprint sprint = sprintRepo.findOneById(sprintId);

//      releaseRepo.findByProductId()
      int previousReleasedIteration = product.getReleases().stream()
          .map(Release::getSprint)
          .mapToInt(Sprint::getIteration)
          .max().orElse(0);
      int toIteration = sprint.getIteration();

      List<Sprint> sprints = sprintRepo.findAllByProductId(sprint.getProductId());
      List<Long> productReleasedItemsIds = sprints.stream()
          .sorted(Comparator.comparing(Sprint::getIteration))
          .filter(s -> s.getIteration() > previousReleasedIteration && s.getIteration() <= toIteration)
          .flatMap(s -> s.getItems().stream())
          .map(SprintBacklogItem::getProductBacklogItemId)
          .collect(toList());
      List<ProductBacklogItem> releasedItems = productBacklogItemRepo.findAllById(productReleasedItemsIds);

      Release release = new Release(product.incrementAndGetVersion() + ".0")
          .setProductId(productId)
          .setSprint(sprint)
          .setReleasedItems(releasedItems)
          .setDate(LocalDate.now());
      product.getReleases().add(release);

      releaseRepo.save(release);
      return release;
   }
}
