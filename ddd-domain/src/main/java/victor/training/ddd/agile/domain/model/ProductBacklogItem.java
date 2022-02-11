package victor.training.ddd.agile.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.AbstractAggregateRoot;
import victor.training.ddd.common.DDD.AggregateRoot;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@Entity
@AggregateRoot
public class ProductBacklogItem extends AbstractAggregateRoot<ProductBacklogItem> {
   @Id
   @GeneratedValue
   private Long id;
   @ManyToOne
   private Product product; // TODO Victor 2022-02-11: NOT allowed since they are different aggregates > link by itd
   //   private Long productId;
   private String title;
   private String description;

   private LocalDateTime createTime = LocalDateTime.now(); // field readonly after creation
   @Version
   private Long version;


   // ------- from here below, only make sense after the BI is added to Spring

   public ProductBacklogItem setId(Long id) {
      this.id = id;
      return this;
   }

   public ProductBacklogItem setProduct(Product product) {
      this.product = product;
      return this;
   }

   public ProductBacklogItem setTitle(String title) {
      this.title = title;
      return this;
   }

   public ProductBacklogItem setDescription(String description) {
      this.description = description;
      return this;
   }

   public ProductBacklogItem setVersion(Long version) {
      this.version = version;
      return this;
   }

}

