package victor.training.ddd.agile;

import lombok.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.common.repo.CustomJpaRepository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.List;

@RestController
@RequiredArgsConstructor
class BacklogItemController {
   private final BacklogItemRepo backlogItemRepo;
   private final ProductRepo productRepo;

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

   @Data
   static class BacklogItemDto {
      public Long id;
      public Long productId;
      public String title;
      public String description;
      public Long version;
   }
}

@Getter
@NoArgsConstructor
@Entity
@DDD.Aggregate
class BacklogItem {
   @Id
   @GeneratedValue
   @Setter
   private Long id;
   @Setter
   private Long productId;
   @Setter
   private String title;
   @Setter
   private String description;
   @Version
   @Setter
   private Long version;

   public BacklogItem(Long productId) {
      this.productId = productId;
   }
}

interface BacklogItemRepo extends CustomJpaRepository<BacklogItem, Long> {
   @Query("SELECT bi " +
          "FROM Sprint s " +
          "JOIN s.items sbi " +
          "JOIN BacklogItem bi  ON sbi.backlogItemId = bi.id " +
          "WHERE s.iteration between ?1 and ?2 " +
          "AND sbi.status = 'DONE'")
   List<BacklogItem> findDoneItemsBetweenIterations(int firstIteration, int lastIteration);
}