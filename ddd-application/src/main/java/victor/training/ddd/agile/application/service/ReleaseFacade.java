package victor.training.ddd.agile.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import victor.training.ddd.agile.application.dto.ReleaseDto;
import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.agile.domain.model.Release;
import victor.training.ddd.agile.domain.model.Sprint;
import victor.training.ddd.agile.domain.repo.ProductRepo;
import victor.training.ddd.agile.domain.repo.ReleaseRepo;
import victor.training.ddd.agile.domain.repo.SprintRepo;
import victor.training.ddd.agile.domain.service.ReleaseService;
import victor.training.ddd.common.DDD.ApplicationService;

@Transactional
//@RestController
@Service
@ApplicationService
@RequiredArgsConstructor
public class ReleaseFacade {
   private final ReleaseRepo releaseRepo;
   private final ProductRepo productRepo;
   private final SprintRepo sprintRepo;
   private final ReleaseService releaseService;

   @PostMapping("product/{productId}/release/{sprintId}")
   public ReleaseDto createRelease(@PathVariable long productId,
                                @PathVariable long sprintId) {
      // validation, conversion and loading some stuff from DB
      Product product = productRepo.findOneById(productId);
      Sprint sprint = sprintRepo.findOneById(sprintId);

      Release release = releaseService.createRelease(product, sprint);

      // sometimes, a .save()
      releaseRepo.save(release);
      return new ReleaseDto(release);
   }

}
