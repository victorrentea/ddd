package victor.training.ddd.agile.web.dto;

import lombok.Data;
import victor.training.ddd.agile.domain.model.Product;

@Data
public class ProductDto {
   public Long id;
   public String code;
   public String name;
   public String mailingList;

   public ProductDto(Product product) {
      id = product.getId();
      name = product.getName();
      code = product.getCode();
      mailingList = product.getTeamMailingList();
   }
}
