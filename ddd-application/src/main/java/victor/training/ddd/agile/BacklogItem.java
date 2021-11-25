package victor.training.ddd.agile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

@Getter
@NoArgsConstructor
@Entity
@DDD.Aggregate
public class BacklogItem {
   @Id
   @GeneratedValue
   @Setter
   private Long id;
   private Long productId;
   private String title;
   private String description;
   @Version
   @Setter
   private Long version;
   private boolean done = false;

   public BacklogItem(Long productId) {
      this.productId = productId;
   }

   public void setDone() {
      done = true;
   }

   public BacklogItem update(String title, String description) {
      if (done) {
         throw new IllegalArgumentException();
      }
      this.title = title;
      this.description = description;
      return this;
   }
}

