package victor.training.ddd.agile.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProductDto {
   public Long id;
   @NotNull
   public String code;

   public String name;
   public String mailingList;
   public String poEmail;
   public String poName;
   public String poPhone;
   public String poUserid;
}