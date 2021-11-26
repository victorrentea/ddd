package victor.training.ddd.agile.web;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.ddd.agile.domain.entity.Product;
import victor.training.ddd.agile.domain.entity.Release;
import victor.training.ddd.agile.domain.entity.Sprint;
import victor.training.ddd.agile.domain.entity.SprintId;
import victor.training.ddd.agile.domain.repo.BacklogItemRepo;
import victor.training.ddd.agile.domain.repo.ProductRepo;
import victor.training.ddd.agile.domain.repo.SprintRepo;

@Transactional
@RestController
@RequiredArgsConstructor
public class ReleaseController {
   private final ProductRepo productRepo;
   private final SprintRepo sprintRepo;
   private final BacklogItemRepo backlogItemRepo;

   @PostMapping("sprint/{sprintId}")
   public Release createRelease(@PathVariable SprintId sprintId) {
      Product product = productRepo.findByCode(sprintId.productCode());
      Sprint sprint = sprintRepo.findOneById(sprintId);


      Release release = product.createRelease(sprint, backlogItemRepo);

      productRepo.save(product);
      return release;
   }

}
