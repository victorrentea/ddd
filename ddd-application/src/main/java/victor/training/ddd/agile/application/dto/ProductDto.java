package victor.training.ddd.agile.application.dto;

import lombok.Data;
import victor.training.ddd.agile.domain.model.Product;

import javax.validation.constraints.NotNull;

@Data

public class ProductDto {
   public Long id;
   // @UniqueCode // custom constraint whose invisible magic Validator calls into DB
   public String code;
   @NotNull
   public String name;
   public String mailingList;
   public String poEmail;
   public String poName;
   public String poPhone;

   public ProductDto() {}
   public ProductDto(Product entity) {
      id = entity.getId();
      name = entity.getName();
      code = entity.getCode();
      mailingList = entity.getTeamMailingList().orElse(null);
   }
}