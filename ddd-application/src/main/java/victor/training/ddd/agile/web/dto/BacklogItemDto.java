package victor.training.ddd.agile.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import victor.training.ddd.agile.domain.model.ProductBacklogItem;

@Data
@NoArgsConstructor
public class BacklogItemDto {
   public Long id;
   public Long productId;
   public String title;
   public String description;
   public Long version;

   public BacklogItemDto(ProductBacklogItem productBacklogItem) {
      id = productBacklogItem.getId();
      productId = productBacklogItem.getProduct().getId();
      description = productBacklogItem.getDescription();
      title = productBacklogItem.getTitle();
      version = productBacklogItem.getVersion();
   }
}
