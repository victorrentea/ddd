package victor.training.ddd.agile.domain.model;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

import static lombok.AccessLevel.PRIVATE;

@Entity
@NoArgsConstructor(access = PRIVATE) // Hibernate
public class ProductBacklogItem {
   @Id
   @GeneratedValue
   private Long id;

   private Long productId;

   // Product + Sprint?
   private String title;
   private String description;
   @Version
   private Long version;

   public ProductBacklogItem(Long productId) {
      this.productId = productId;
   }

   public Long getId() {
      return id;
   }

   public Long getVersion() {
      return version;
   }

   public Long getProductId() {
      return productId;
   }

   public String getTitle() {
      return title;
   }

   public String getDescription() {
      return description;
   }

   public ProductBacklogItem setVersion(Long version) {
      this.version = version;
      return this;
   }

   public ProductBacklogItem setContents(String title, String description) {
      this.title = title;
      this.description = description;
      return this;
   }

}
