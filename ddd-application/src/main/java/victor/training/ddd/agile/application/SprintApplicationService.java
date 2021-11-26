package victor.training.ddd.agile.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import victor.training.ddd.agile.domain.entity.Product;
import victor.training.ddd.agile.domain.entity.Sprint;
import victor.training.ddd.agile.domain.entity.SprintId;
import victor.training.ddd.agile.domain.repo.ProductRepo;
import victor.training.ddd.agile.domain.repo.SprintRepo;
import victor.training.ddd.agile.web.SprintController.SprintDto;

@RequiredArgsConstructor
@Service
public class SprintApplicationService {
   private final ProductRepo productRepo;
   private final SprintRepo sprintRepo;

   public SprintId createSprint(SprintDto dto) {
      Product product = productRepo.findOneById(dto.productId);

      Sprint sprint = product.createSprint(dto.plannedEnd);
      return sprintRepo.save(sprint).getId();
   }

   public Sprint getSprint(@PathVariable SprintId id) {
      return sprintRepo.findOneById(id);
   }
}


class PlaceOrderApplicationService { // are 2k de linii:>>
   // tot nu scapi de riscul de a degenera in clase gigantice

}