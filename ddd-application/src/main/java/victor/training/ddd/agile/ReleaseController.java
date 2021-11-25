package victor.training.ddd.agile;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequiredArgsConstructor
public class ReleaseController {
   private final ReleaseRepo releaseRepo;
   private final ProductRepo productRepo;
   private final SprintRepo sprintRepo;
   private final BacklogItemRepo backlogItemRepo;

   @PostMapping("sprint/{sprintId}")
   public Release createRelease(@PathVariable SprintId sprintId) {
      Product product = productRepo.findByCode(sprintId.productCode());
      Sprint sprint = sprintRepo.findOneById(sprintId);


      Release release = product.createRelease(sprint, backlogItemRepo);

      releaseRepo.save(release);
      return release;
   }

}
