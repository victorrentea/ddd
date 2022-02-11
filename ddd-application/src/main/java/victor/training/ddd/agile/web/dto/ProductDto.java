package victor.training.ddd.agile.web.dto;

import lombok.Data;

@Data
public class ProductDto {
   public Long id;
   public String code;
   public String name;
   public String mailingList;
}
