package victor.training.ddd.agile.application.dto;

import lombok.Data;

@Data
   public class BacklogItemDto {
      public Long id;
      public Long productId;
      public String title;
      public String description;
      public Long version;
   }