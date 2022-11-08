package victor.training.ddd.agile.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.ddd.agile.domain.model.*;
import victor.training.ddd.agile.domain.repo.BacklogItemRepo;
import victor.training.ddd.agile.domain.repo.ProductRepo;
import victor.training.ddd.agile.domain.repo.ReleaseRepo;
import victor.training.ddd.agile.domain.repo.SprintRepo;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@Transactional
@RestController
@RequiredArgsConstructor
@Validated // needed as tests call directly these methods
public class ReleaseApplicationService {
   private final ReleaseRepo releaseRepo;
   private final ProductRepo productRepo;
   private final SprintRepo sprintRepo;
   private final BacklogItemRepo backlogItemRepo;

   @PostMapping("product/{productId}/release/{sprintId}")
   public Release createRelease(@PathVariable long productId, @PathVariable long sprintId) {
      Product product = productRepo.findOneById(productId);
      Sprint sprint = sprintRepo.findOneById(sprintId);

      int previouslyReleasedIteration = product.getReleases().stream()
          .map(Release::getSprint)
          .mapToInt(Sprint::getIteration)
          .max().orElse(0);
      int releasedIteration = sprint.getIteration();

      // TODO review

      List<Sprint> sprintsForProduct = sprintRepo.findByProductId(product.getId());
      List<SprintItem> releasedItems = sprintsForProduct.stream()
          .sorted(Comparator.comparing(Sprint::getIteration))
          .filter(s -> s.getIteration() > previouslyReleasedIteration
                       && s.getIteration() <= releasedIteration)
          .flatMap(s -> s.getItems().stream())
          .collect(Collectors.toList());


      List<BacklogItem> backlogItems = backlogItemRepo.findAllById(releasedItems.stream().map(SprintItem::getBacklogItemId).collect(Collectors.toList()));

      String releaseNotes = backlogItems.stream().map(BacklogItem::getTitle).collect(joining("\n"));

      Release release = new Release()
          .setProduct(product)
          .setSprint(sprint)
          .setReleaseNotes(releaseNotes)
          .setDate(LocalDate.now())
          .setVersion(product.incrementAndGetVersion() + ".0");
      product.getReleases().add(release);

      releaseRepo.save(release);
      return release;
   }
}
