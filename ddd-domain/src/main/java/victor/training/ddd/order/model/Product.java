package victor.training.ddd.order.model;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

//
@Document
@Getter
public class Product {
   // imagine, shipped by, descr, carac MAP,
   private String id;
   private final int price;

   public String getId() {
      return id;
   }

   public Product(int price) {
      this.price = price;
   }

   public int getPrice() {
      return price;
   }

}
