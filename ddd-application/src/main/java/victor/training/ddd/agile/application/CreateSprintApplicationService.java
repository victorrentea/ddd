package victor.training.ddd.agile.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import victor.training.ddd.agile.*;
import victor.training.ddd.agile.SprintController.SprintDto;

@RequiredArgsConstructor
@Service
public class CreateSprintApplicationService {
      private final ProductRepo productRepo;
      private final SprintRepo sprintRepo;

   public SprintId createSprint(SprintDto dto) {
      Product product = productRepo.findOneById(dto.productId);

      Sprint sprint = product.createSprint(dto.plannedEnd);
      return sprintRepo.save(sprint).getId();
   }

//}
//class GetSprintByIdApplicationService { // NU IMI PLACE :prea mici clase> overengineering
   public Sprint getSprint(@PathVariable SprintId id) {
      return sprintRepo.findOneById(id);
   }
}



class PlaceOrderApplicationService { // are 2k de linii:>>
   // tot nu scapi de riscul de a degenera in clase gigantice

}