package victor.training.ddd.agile.dto;

import lombok.Data;

@Data
public class BacklogItemDto {
   public Long id;
   public Long productId;
   public String title;
   public String description;
   public Long version;
}