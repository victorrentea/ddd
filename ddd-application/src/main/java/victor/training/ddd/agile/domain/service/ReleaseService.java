package victor.training.ddd.agile.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.ddd.agile.domain.MyException;
import victor.training.ddd.agile.domain.MyException.ErrorCode;
import victor.training.ddd.agile.domain.MySubclass;
import victor.training.ddd.agile.domain.model.*;
import victor.training.ddd.agile.domain.repo.ProductBacklogItemRepo;
import victor.training.ddd.agile.domain.repo.ReleaseRepo;
import victor.training.ddd.agile.domain.repo.SprintRepo;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ReleaseService {
   private final ReleaseRepo releaseRepo;
   private final SprintRepo sprintRepo;
   private final ProductBacklogItemRepo productBacklogItemRepo;

   // GOAL: implement your central complex logic on YOUR OWM model!
   // when logic in UC grows, if you extract it, you SHOULD SEE see that it talks in terms of your DOMAIN
   // DO NOT EVER IMPLEMENT LOGIC on DTOs
   public Release createRelease(Product product, Sprint sprint) {
      List<Release> releases = releaseRepo.findAllByProductId(product.getId());
      int previousReleasedIteration = releases.stream()
          .map(Release::getSprint)
          .mapToInt(Sprint::getIteration)
          .max().orElse(0);

      if (false) {
         throw new MyException(ErrorCode.CUSTOMER_NAME_TOO_SHORT);
      }
      try {

      } catch (MySubclass mySubclass) { // the only reason for subclassing a base exception

      }

      List<Sprint> sprints = sprintRepo.findAllByProductId(product.getId());
      List<Long> productReleasedItemsIds = sprints.stream()
          .sorted(Comparator.comparing(Sprint::getIteration))
          .filter(s -> s.getIteration() > previousReleasedIteration && s.getIteration() <= sprint.getIteration())
          .flatMap(s -> s.getItems().stream())
          .map(SprintBacklogItem::getProductBacklogItemId)
          .collect(toList());
      List<ProductBacklogItem> releasedItems = productBacklogItemRepo.findAllById(productReleasedItemsIds);

      return new Release(product.incrementAndGetVersion() + ".0")
          .setProductId(product.getId())
          .setSprint(sprint)
          .setReleasedItems(releasedItems)
          .setDate(LocalDate.now());
   }
}
