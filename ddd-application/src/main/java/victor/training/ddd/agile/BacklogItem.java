package victor.training.ddd.agile;

import lombok.*;
import org.springframework.context.event.EventListener;
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
      BacklogItem item = backlogItemRepo.findOneById(event.getBacklogItemId());
      item.setDone();
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
   private Long productId;
   private String title;
   private String description;
   @Version
   @Setter
   private Long version;
   private boolean done = false;

   public BacklogItem(Long productId) {
      this.productId = productId;
   }

   public void setDone() {
      done = true;
   }

   public BacklogItem update(String title, String description) {
      if (done) {
         throw new IllegalArgumentException();
      }
      this.title = title;
      this.description = description;
      return this;
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