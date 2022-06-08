package victor.training.ddd.agile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.ddd.agile.entity.*;
import victor.training.ddd.agile.repo.ProductRepo;
import victor.training.ddd.agile.repo.ReleaseRepo;
import victor.training.ddd.agile.repo.SprintRepo;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RestController
@RequiredArgsConstructor
@Validated // needed as tests call directly these methods
public class ReleaseService {
   private final ReleaseRepo releaseRepo;
   private final ProductRepo productRepo;
   private final SprintRepo sprintRepo;

   @PostMapping("product/{productId}/release/{sprintId}")
   public Release createRelease(@PathVariable long productId, @PathVariable long sprintId) {
      Product product = productRepo.findOneById(productId);
      Sprint sprint = sprintRepo.findOneById(sprintId);

      int previouslyReleasedIteration = product.getReleases().stream()
          .map(Release::getSprint)
          .mapToInt(Sprint::getIteration)
          .max().orElse(0);
      int releasedIteration = sprint.getIteration();

      List<SprintItem> releasedItems = product.getSprints().stream()
          .sorted(Comparator.comparing(Sprint::getIteration))
          .filter(s -> s.getIteration() > previouslyReleasedIteration
                       && s.getIteration() <= releasedIteration)
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
