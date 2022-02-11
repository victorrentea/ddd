package victor.training.ddd.agile.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.domain.event.BacklogItemTitleChangedEvent;
import victor.training.ddd.agile.domain.model.BacklogItem;
import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.agile.domain.repo.BacklogItemRepo;
import victor.training.ddd.agile.domain.repo.ProductRepo;
import victor.training.ddd.agile.web.dto.BacklogItemDto;

@RestController
@RequiredArgsConstructor
public class BacklogItemController {
   private final BacklogItemRepo backlogItemRepo;
   private final ProductRepo productRepo;
   private final ApplicationEventPublisher publisher;

   @PostMapping("backlog")
   @Transactional
   public Long createBacklogItem(@RequestBody BacklogItemDto dto) {
      Product product = productRepo.findOneById(dto.productId);
      BacklogItem backlogItem = new BacklogItem()
          .setProduct(product)
          .setDescription(dto.description)
          .setTitle(dto.title);
      product.getBacklogItems().add(backlogItem);
      return backlogItemRepo.save(backlogItem).getId();
   }

   @GetMapping("backlog/{id}")
   public BacklogItemDto getBacklogItem(@PathVariable long id) {
      BacklogItem backlogItem = backlogItemRepo.findOneById(id);
      return new BacklogItemDto(backlogItem);
   }


   @PutMapping("backlog")
   @Transactional
   public void updateBacklogItem(@RequestBody BacklogItemDto dto) {
      BacklogItem oldItem = backlogItemRepo.findOneById(dto.id);

     if (!oldItem.getTitle().equals(dto.title)) { // TODO vrentea 2022-02-11 ORDER matters
         publisher.publishEvent(new BacklogItemTitleChangedEvent(dto.id));
      }

      BacklogItem backlogItem = new BacklogItem()
          .setId(dto.id)
          .setProduct(productRepo.findOneById(dto.productId)) // TODO vrentea 2022-02-11 reasonable?
          .setDescription(dto.description)
          .setTitle(dto.title)
          .setVersion(dto.version); // TODO vrentea 2022-02-11 readonly fields in original entity are lost
      backlogItemRepo.save(backlogItem); // TODO vrentea 2022-02-11 does MERGE now, move to updating attached entity?
   }

   @DeleteMapping("backlog/{id}")
   public void deleteBacklogItem(@PathVariable long id) {
      backlogItemRepo.deleteById(id);
   }
}
