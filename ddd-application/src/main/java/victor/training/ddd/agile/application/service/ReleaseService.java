package victor.training.ddd.agile.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.ddd.agile.domain.model.BacklogItem;
import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.agile.domain.model.Release;
import victor.training.ddd.agile.domain.model.Sprint;
import victor.training.ddd.agile.domain.repo.ProductRepo;
import victor.training.ddd.agile.domain.repo.ReleaseRepo;
import victor.training.ddd.agile.domain.repo.SprintRepo;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RestController
@RequiredArgsConstructor
public class ReleaseService {
   private final ReleaseRepo releaseRepo;
   private final ProductRepo productRepo;
   private final SprintRepo sprintRepo;

   @PostMapping("product/{productId}/release/{sprintId}")
   public Release createRelease(@PathVariable long productId, @PathVariable long sprintId) {
      Product product = productRepo.findOneById(productId);
      Sprint sprint = sprintRepo.findOneById(sprintId);

      int fromIteration = product.getReleases().stream()
          .map(Release::getSprint)
          .mapToInt(Sprint::getIteration)
          .max().orElse(0);
      int toIteration = sprint.getIteration();

      List<Sprint> sprints = sprintRepo.findAllByProductId(sprint.getProductId());
      List<BacklogItem> releasedItems = sprints.stream()
          .sorted(Comparator.comparing(Sprint::getIteration))
          .filter(s -> s.getIteration() > fromIteration && s.getIteration() <= toIteration)
          .flatMap(s -> s.getItems().stream())
          .collect(Collectors.toList());

      Release release = new Release()
          .setProduct(product)
          .setSprint(sprint)
          .setReleasedItems(releasedItems)
          .setDate(LocalDate.now())
          .setVersion(product.incrementAndGetVersion() + ".0");
      product.getReleases().add(release);

      releaseRepo.save(release);
      return release;
   }
}
