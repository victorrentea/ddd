package victor.training.ddd.agile.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.ddd.agile.domain.event.BacklogItemTitleChangedEvent;
import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.agile.domain.model.Release;
import victor.training.ddd.agile.domain.model.Sprint;
import victor.training.ddd.agile.domain.repo.ProductRepo;
import victor.training.ddd.agile.domain.repo.SprintRepo;

@RestController
@RequiredArgsConstructor
public class ReleaseController {
   private final ProductRepo productRepo;
   private final SprintRepo sprintRepo;

   @PostMapping("product/{productId}/release/{sprintId}")
   public Long createRelease(@PathVariable long productId, @PathVariable long sprintId) {
      Product product = productRepo.findOneById(productId);
      Sprint sprint = sprintRepo.findOneById(sprintId);

      Long newReleaseId = product.addRelease(sprint);

      productRepo.save(product);
      return newReleaseId;
   }


   //   @Async
   @EventListener // TODO Victor 2022-02-11: implement (at end)
   public void onBacklogItemTitleChanged(BacklogItemTitleChangedEvent event) {
      Long backlogItemId = event.getBacklogItemId();
      // determine relase to update
      Release release;
//      release.setReleaseNotes(computeReleaseNotes(backlogItem...sprint))
   }


}
