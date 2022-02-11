package victor.training.ddd.agile.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import victor.training.ddd.agile.domain.model.BacklogItem;

@Data
@NoArgsConstructor
public class BacklogItemDto {
   public Long id;
   public Long productId;
   public String title;
   public String description;
   public Long version;

   public BacklogItemDto(BacklogItem backlogItem) {
      id = backlogItem.getId();
      productId = backlogItem.getProduct().getId();
      description = backlogItem.getDescription();
      title = backlogItem.getTitle();
      version = backlogItem.getVersion();
   }
}
