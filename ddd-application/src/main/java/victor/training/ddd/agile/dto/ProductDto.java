package victor.training.ddd.agile.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProductDto {
   private Long id;
   @NotNull
   @NotBlank
   private String code;
   @NotNull
   @NotBlank
   private String name;
   private String mailingList;
   private String poEmail;
   private String poName;
   private String poPhone;

}