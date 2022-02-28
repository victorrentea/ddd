package victor.training.ddd.agile.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.application.dto.BacklogItemDto;
import victor.training.ddd.agile.domain.model.ProductBacklogItem;
import victor.training.ddd.agile.domain.repo.ProductBacklogItemRepo;

@RestController
@RequiredArgsConstructor
public class BacklogItemService {
   private final ProductBacklogItemRepo productBacklogItemRepo;

   @PostMapping("backlog")
   @Transactional
   public Long createBacklogItem(@RequestBody BacklogItemDto dto) {
      ProductBacklogItem productBacklogItem = new ProductBacklogItem(dto.productId)
          .setContents(dto.title, dto.description)
          ;

      return productBacklogItemRepo.save(productBacklogItem).getId();
   }

   @GetMapping("backlog/{id}")
   public BacklogItemDto getBacklogItem(@PathVariable long id) {
      ProductBacklogItem productBacklogItem = productBacklogItemRepo.findOneById(id);
      BacklogItemDto dto = new BacklogItemDto();
      dto.id = productBacklogItem.getId();

      dto.productId = productBacklogItem.getProductId();
      dto.description = productBacklogItem.getDescription();
      dto.title = productBacklogItem.getTitle();
      dto.version = productBacklogItem.getVersion();
      return dto;
   }

   @PutMapping("backlog")
   @Transactional
   public void updateBacklogItem(@RequestBody BacklogItemDto dto) {
      // TODO if Backlog Item is COMPLETED, reject the update
      ProductBacklogItem item = productBacklogItemRepo.findOneById(dto.id);
      item
          .setContents(dto.title, dto.description)
          .setVersion(dto.version);
   }

   @DeleteMapping("backlog/{id}")
   public void deleteBacklogItem(@PathVariable long id) {
      productBacklogItemRepo.deleteById(id);
   }
}
