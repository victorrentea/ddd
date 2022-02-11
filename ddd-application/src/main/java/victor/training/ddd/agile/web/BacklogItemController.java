package victor.training.ddd.agile.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.domain.model.ProductBacklogItem;
import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.agile.domain.repo.ProductBacklogItemRepo;
import victor.training.ddd.agile.domain.repo.ProductRepo;
import victor.training.ddd.agile.web.dto.BacklogItemDto;

@RestController
@RequiredArgsConstructor
public class BacklogItemController {
   private final ProductBacklogItemRepo productBacklogItemRepo;
   private final ProductRepo productRepo;
   private final ApplicationEventPublisher publisher;

   @PostMapping("backlog")
   @Transactional
   public Long createBacklogItem(@RequestBody BacklogItemDto dto) {
      Product product = productRepo.findOneById(dto.productId);
      ProductBacklogItem productBacklogItem = new ProductBacklogItem()
          .setProduct(product) // replace with productId
          .setDescription(dto.description)
          .setTitle(dto.title);
      product.getProductBacklogItems().add(productBacklogItem); // die
      return productBacklogItemRepo.save(productBacklogItem).getId();
   }

   @GetMapping("backlog/{id}")
   public BacklogItemDto getBacklogItem(@PathVariable long id) {
      return new BacklogItemDto(productBacklogItemRepo.findOneById(id));
   }

   @PutMapping("backlog")
   @Transactional
   public void updateBacklogItem(@RequestBody BacklogItemDto dto) {

      ProductBacklogItem productBacklogItem = new ProductBacklogItem()
          .setId(dto.id)
          .setProduct(productRepo.findOneById(dto.productId)) // TODO vrentea 2022-02-11 risky
          .setDescription(dto.description)
          .setTitle(dto.title)
          .setVersion(dto.version)
          ; // TODO vrentea 2022-02-11 readonly fields in original entity are lost

      productBacklogItemRepo.save(productBacklogItem); // TODO vrentea 2022-02-11 does MERGE now, move to updating an entity I load from DB. And then use (a) autoflush or (b) explicit save back
   }

   @DeleteMapping("backlog/{id}")
   public void deleteBacklogItem(@PathVariable long id) {
      productBacklogItemRepo.deleteById(id);
   }
}
