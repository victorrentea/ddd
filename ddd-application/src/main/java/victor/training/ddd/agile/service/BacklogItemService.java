package victor.training.ddd.agile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.entity.BacklogItem;
import victor.training.ddd.agile.entity.Product;
import victor.training.ddd.agile.dto.BacklogItemDto;
import victor.training.ddd.agile.repo.BacklogItemRepo;
import victor.training.ddd.agile.repo.ProductRepo;

@RestController
@RequiredArgsConstructor
public class BacklogItemService {
   private final BacklogItemRepo backlogItemRepo;
   private final ProductRepo productRepo;

   @PostMapping("backlog")
   @Transactional
   public Long createBacklogItem(@RequestBody BacklogItemDto dto) {
      Product product = productRepo.findOneById(dto.productId);
      BacklogItem backlogItem = new BacklogItem()
          .setProduct(product)
          .setDescription(dto.description)
          .setTitle(dto.title);
      return backlogItemRepo.save(backlogItem).getId();
   }

   @GetMapping("backlog/{id}")
   public BacklogItemDto getBacklogItem(@PathVariable long id) {
      BacklogItem backlogItem = backlogItemRepo.findOneById(id);
      BacklogItemDto dto = new BacklogItemDto();
      dto.id = backlogItem.getId();
      dto.productId = backlogItem.getProduct().getId();
      dto.description = backlogItem.getDescription();
      dto.title = backlogItem.getTitle();
      dto.version = backlogItem.getVersion();
      return dto;
   }

   @PutMapping("backlog")
   public void updateBacklogItem(@RequestBody BacklogItemDto dto) {
      // TODO if Backlog Item is COMPLETED, reject the update
      BacklogItem backlogItem = new BacklogItem()
          .setId(dto.id)
          .setProduct(productRepo.findOneById(dto.productId))
          .setDescription(dto.description)
          .setTitle(dto.title)
          .setVersion(dto.version);
      backlogItemRepo.save(backlogItem);
   }

   @DeleteMapping("backlog/{id}")
   public void deleteBacklogItem(@PathVariable long id) {
      backlogItemRepo.deleteById(id);
   }
}
