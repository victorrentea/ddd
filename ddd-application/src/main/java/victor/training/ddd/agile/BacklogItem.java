package victor.training.ddd.agile;

import lombok.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.common.repo.CustomJpaRepository;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@RestController
@RequiredArgsConstructor
class BacklogItemController {
   private final BacklogItemRepo backlogItemRepo;
   private final ProductRepo productRepo;

   @Data
   static class BacklogItemDto {
      public Long id;
      public Long productId;
      public String title;
      public String description;
      public Long version;
   }

   @PostMapping("backlog")
   @Transactional
   public Long createBacklogItem(@RequestBody BacklogItemDto dto) {
      Product product = productRepo.findOneById(dto.productId);
      BacklogItem backlogItem = new BacklogItem()
          .setProductId(product.getId())
          .setDescription(dto.description)
          .setTitle(dto.title);

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
   @Transactional
   public void updateBacklogItem(@RequestBody BacklogItemDto dto) {
      BacklogItem backlogItem = new BacklogItem()
          .setId(dto.id)
          .setProductId(dto.productId)
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

@Getter
@Setter
@NoArgsConstructor
@Entity
@DDD.Aggregate
class BacklogItem {
   @Id
   @GeneratedValue
   private Long id;
   private Long productId;
   private String title;
   private String description;



   @Version
   private Long version;
}

interface BacklogItemRepo extends CustomJpaRepository<BacklogItem, Long> {
}