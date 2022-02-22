package victor.training.ddd.agile;

import lombok.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.common.repo.CustomJpaRepository;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@RestController
@RequiredArgsConstructor
class BacklogItemService {
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

@Getter
@Setter
@NoArgsConstructor
@Entity
class BacklogItem {
   @Id
   @GeneratedValue
   private Long id;
   @ManyToOne
   private Product product;
   private String title;
   private String description;

   public enum Status {
      CREATED,
      STARTED,
      DONE
   }
   @Enumerated(STRING)
   private Status status = Status.CREATED;

   @ManyToOne
   private Sprint sprint; // ⚠ not NULL when assigned to a sprint
   private Integer fpEstimation; // ⚠ not NULL when assigned to a sprint
   private Integer hoursConsumed; // ⚠ not NULL when assigned to a sprint

   @Version
   private Long version;

   public void addHours(int hours) {
      hoursConsumed += hours;
   }

}

interface BacklogItemRepo extends CustomJpaRepository<BacklogItem, Long> {
}