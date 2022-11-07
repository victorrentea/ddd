package victor.training.ddd.agile.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ProductDto {
   private Long id;

   //@UniqueInDB // ==> smell: SELECT/API ?!! via an annotation <- not recommended
   @NotNull
   @Size(min = 3)
   private String code;
   @NotNull
   @Size(min = 2)
   private String name;

   private String mailingList;

   private String poName;
   private String poEmail;
   private String poPhone;

//   @AssertTrue
//   public boolean method() {
//      return poEmail != null || poPhone != null;
//   }
}