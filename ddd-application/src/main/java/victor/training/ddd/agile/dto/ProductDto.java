package victor.training.ddd.agile.dto;

import lombok.Data;

@Data
public class ProductDto {
   private Long id;
   private String code;
   private String name;
   private String mailingList;
   private String poEmail;
   private String poName;
   private String poPhone;

}