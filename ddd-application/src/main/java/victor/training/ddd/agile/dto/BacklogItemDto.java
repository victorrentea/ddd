package victor.training.ddd.agile.dto;

import lombok.Data;

@Data
public class BacklogItemDto {
   private Long id;
   private Long productId;
   private String title;
   private String description;
   private Long version;
}