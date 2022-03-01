package victor.training.ddd.agile.dto;

import lombok.Data;

@Data
public class ProductDto {
   public Long id;
   public String code;
   public String name;
   public String mailingList;
   public String poEmail;
   public String poName;
   public String poPhone;
}