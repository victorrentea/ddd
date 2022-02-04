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

   @ManyToOne
   private Sprint sprint; // ⚠ not NULL when assigned to a sprint
   private Integer fpEstimation; // ⚠ not NULL when assigned to a sprint

   public enum Status {
      CREATED,
      STARTED,
      DONE
   }

   @Enumerated(STRING)
   private Status status = Status.CREATED;
   @Version
   private Long version;

   private int hoursConsumed;

   public void start() {
      if (this.status != Status.CREATED) {
         throw new IllegalStateException("Item already started");
      }
      this.status = Status.STARTED;
   }

   public void complete() {
      if (this.status != Status.STARTED) {
         throw new IllegalStateException("Cannot complete an Item before starting it");
      }
      this.status = Status.DONE;
   }


   public void addHours(int hours) {
      hoursConsumed += hours;
   }

   public BacklogItem setId(Long id) {
      this.id = id;
      return this;
   }

   public BacklogItem setProduct(Product product) {
      this.product = product;
      return this;
   }

   public BacklogItem setTitle(String title) {
      this.title = title;
      return this;
   }

   public BacklogItem setDescription(String description) {
      this.description = description;
      return this;
   }

   public BacklogItem setSprint(Sprint sprint) {
      this.sprint = sprint;
      return this;
   }

   public BacklogItem setFpEstimation(Integer fpEstimation) {
      this.fpEstimation = fpEstimation;
      return this;
   }

   public BacklogItem setVersion(Long version) {
      this.version = version;
      return this;
   }

   public BacklogItem setHoursConsumed(int hoursConsumed) {
      this.hoursConsumed = hoursConsumed;
      return this;
   }
}

interface BacklogItemRepo extends CustomJpaRepository<BacklogItem, Long> {
}