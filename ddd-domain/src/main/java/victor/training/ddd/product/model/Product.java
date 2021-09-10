package victor.training.ddd.product.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static java.util.Objects.requireNonNull;

@Document
public class Product {
   @Id
   private String id;

   private String name;

   // if there are more things related to price, I would create a ProductPriceVO with 2-3-4 fields.
   private ProductPrice price = new ProductPrice();

   private Product() {}

   public Product(String id, String name, ProductPrice price) {
      this.id = requireNonNull(id);
      this.name = requireNonNull(name);
      this.price = requireNonNull(price);
   }

   public String id() {
      return id;
   }

   public String name() {
      return name;
   }

   public ProductPrice price() {
      return price;
   }

   // Expect MORE attributes and logic to add here
}
