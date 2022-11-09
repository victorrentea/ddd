package victor.training.ddd.agile.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.domain.model.BacklogItem;
import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.agile.application.dto.BacklogItemDto;
import victor.training.ddd.agile.domain.repo.BacklogItemRepo;
import victor.training.ddd.agile.domain.repo.ProductRepo;

@RestController
@RequiredArgsConstructor
public class BacklogItemApplicationService {
   private final BacklogItemRepo backlogItemRepo;
   private final ProductRepo productRepo;

   @PostMapping("backlog")
   @Transactional
   public Long createBacklogItem(@RequestBody BacklogItemDto dto) {
      Product product = productRepo.findOneById(dto.getProductId());
      BacklogItem backlogItem = new BacklogItem(product, dto.getTitle(), dto.getDescription());
      return backlogItemRepo.save(backlogItem).getId();
   }

   @GetMapping("backlog/{id}")
   public BacklogItemDto getBacklogItem(@PathVariable long id) {
      return new BacklogItemDto(backlogItemRepo.findOneById(id));
   }

   @PutMapping("backlog")
   public void updateBacklogItem(@RequestBody BacklogItemDto dto) {
      // TODO if Backlog Item is COMPLETED, reject the update
      Product product = productRepo.findOneById(dto.getProductId());
      BacklogItem backlogItem = new BacklogItem(product, dto.getTitle(), dto.getDescription())
          .setId(dto.getId())
          .setVersion(dto.getVersion());
      backlogItemRepo.save(backlogItem);
      //instead
      BacklogItem bi = backlogItemRepo.findOneById(dto.getId());
//      bi.set.....

      // TDO  merge vs update ?
   }

   @DeleteMapping("backlog/{id}")
   public void deleteBacklogItem(@PathVariable long id) {
      backlogItemRepo.deleteById(id);
   }
}
