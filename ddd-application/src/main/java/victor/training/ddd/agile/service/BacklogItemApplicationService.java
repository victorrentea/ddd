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
public class BacklogItemApplicationService {
   private final BacklogItemRepo backlogItemRepo;
   private final ProductRepo productRepo;

   @PostMapping("backlog")
   @Transactional
   public Long createBacklogItem(@RequestBody BacklogItemDto dto) {
      Product product = productRepo.findOneById(dto.getProductId());
      BacklogItem backlogItem = new BacklogItem()
          .setProduct(product)
          .setDescription(dto.getDescription())
          .setTitle(dto.getTitle());
      return backlogItemRepo.save(backlogItem).getId();
   }

   @GetMapping("backlog/{id}")
   public BacklogItemDto getBacklogItem(@PathVariable long id) {
      BacklogItem backlogItem = backlogItemRepo.findOneById(id);
      return new BacklogItemDto()
              .setId(backlogItem.getId())
              .setProductId(backlogItem.getProduct().getId())
              .setDescription(backlogItem.getDescription())
              .setTitle(backlogItem.getTitle())
              .setVersion(backlogItem.getVersion());
   }

   @PutMapping("backlog")
   public void updateBacklogItem(@RequestBody BacklogItemDto dto) {
      // TODO if Backlog Item is COMPLETED, reject the update
      BacklogItem backlogItem = new BacklogItem()
          .setId(dto.getId())
          .setProduct(productRepo.findOneById(dto.getProductId()))
          .setDescription(dto.getDescription())
          .setTitle(dto.getTitle())
          .setVersion(dto.getVersion());
      backlogItemRepo.save(backlogItem);
   }

   @DeleteMapping("backlog/{id}")
   public void deleteBacklogItem(@PathVariable long id) {
      backlogItemRepo.deleteById(id);
   }
}
