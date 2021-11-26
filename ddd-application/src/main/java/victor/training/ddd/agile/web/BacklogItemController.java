package victor.training.ddd.agile.web;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.infra.BacklogItemCompletedMessage;
import victor.training.ddd.agile.domain.entity.BacklogItem;
import victor.training.ddd.agile.domain.entity.Product;
import victor.training.ddd.agile.domain.event.BacklogItemCompletedEvent;
import victor.training.ddd.agile.domain.repo.BacklogItemRepo;
import victor.training.ddd.agile.domain.repo.ProductRepo;

@RestController
@RequiredArgsConstructor
public class BacklogItemController {
   private final BacklogItemRepo backlogItemRepo;
   private final ProductRepo productRepo;

   @PostMapping("backlog")
   @Transactional
   public Long createBacklogItem(@RequestBody BacklogItemDto dto) {
      Product product = productRepo.findOneById(dto.productId);
//      BacklogItem backlogItem = product.createItem()
//          .update(dto.title, dto.description);
      BacklogItem backlogItem = new BacklogItem(product.getId())
          .update(dto.title, dto.description);
      return backlogItemRepo.save(backlogItem).getId();
   }

   @GetMapping("backlog/{id}")
   public BacklogItemDto getBacklogItem(@PathVariable long id) {
      BacklogItem backlogItem = backlogItemRepo.findOneById(id);
      BacklogItemDto dto = new BacklogItemDto();
      dto.id = backlogItem.getId();
      dto.productId = backlogItem.getProductId();
      dto.description = backlogItem.getDescription();
      dto.title = backlogItem.getTitle();
      dto.version = backlogItem.getVersion();
      return dto;
   }

   @PutMapping("backlog")
   public void updateBacklogItem(@RequestBody BacklogItemDto dto) {
      BacklogItem backlogItem = backlogItemRepo.findOneById(dto.id)
          .update(dto.title, dto.description) // arunce exceptie daca item e deja DONE in vreun sprint
          .setVersion(dto.version);

      backlogItemRepo.save(backlogItem);
   }

   @EventListener
   public void handleItemCompleted(BacklogItemCompletedEvent event) {
//      eventDispatcher.registerListener(BacklogItemCompletedEvent.class, event  -> { // java EE
//     messageSender.send(new BacklogItemCompletedMessage(event.getBacklogItemId()));
      // TODO mesaj pe coada.
      // send pe queue si apoi procesarea continua ...
   }

   // vreau sa decuplez asa de adanc intre Sprint Agg si BacklogItem Agg incat in 2 luni sa
   // mut BacklogItem in propriul microserviciu (e in buget deja aprobat)
   // receive pe queue esti in alta tranzactie
   @ServiceActivator()
   public void handleItemCompleted(BacklogItemCompletedMessage message) {
      BacklogItem item = backlogItemRepo.findOneById(message.getBacklogItemId());
      item.setDone();
      backlogItemRepo.save(item);
   }


   @DeleteMapping("backlog/{id}")
   public void deleteBacklogItem(@PathVariable long id) {
      backlogItemRepo.deleteById(id);
   }

   @Data
   public static class BacklogItemDto {
      public Long id;
      public Long productId;
      public String title;
      public String description;
      public Long version;
   }
}
