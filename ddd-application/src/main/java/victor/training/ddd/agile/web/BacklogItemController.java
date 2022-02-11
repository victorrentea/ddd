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
      BacklogItemDto dto = new BacklogItemDto();
      dto.id = backlogItem.getId();
      dto.productId = backlogItem.getProduct().getId();
      dto.description = backlogItem.getDescription();
      dto.title = backlogItem.getTitle();
      dto.version = backlogItem.getVersion();
      return dto;
   }


   @PutMapping("backlog")
   @Transactional
   public void updateBacklogItem(@RequestBody BacklogItemDto dto) {

      BacklogItem oldItem = backlogItemRepo.findOneById(dto.id);
      if (!oldItem.getTitle().equals(dto.title)) {
         publisher.publishEvent(new BacklogItemTitleChangedEvent(dto.id));
//         Product product = productRepo.findOneById(dto.productId);
//
////         Optional<Release> release = product.findReleaseForIteration(oldItem.getSprint().getId());
//         // TODO homework
//         Release release;
//         release.setReleaseNotes(????)
      }

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
