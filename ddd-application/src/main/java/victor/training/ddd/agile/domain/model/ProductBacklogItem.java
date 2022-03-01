package victor.training.ddd.agile.domain.model;

import lombok.NoArgsConstructor;
import victor.training.ddd.common.DDD.AggregateRoot;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@AggregateRoot
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

   public ProductBacklogItem(Long productId, String title, String description) {
      this.productId = productId;
      setContents(title, description);
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
      this.title = requireNonNull(title);
      this.description = requireNonNull(description);
      return this;
   }

}