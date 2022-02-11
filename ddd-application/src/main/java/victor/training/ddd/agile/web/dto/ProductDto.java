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

   //   Repo repo;
   public Product toEntity() { // noble goal to keep mapping static < doable if DDD more aggresively
      // in readlity have 10 params constructor
      // BREAK THE MODEL FURHTER. MORE VALUE OBJECT 10 > 7
      return new Product(
          code,
          name,
          mailingList); // TODO vrentea 2022-02-11 TWO
   }
}
