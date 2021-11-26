package victor.training.ddd.agile.domain.entity;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class SprintId implements Serializable {
   private String id;

   protected SprintId() {
   }

   public SprintId(String productCode, int iteration) {
      if (productCode.length() != 3) {
         throw new IllegalArgumentException();
      }
      if (iteration < 0) {
         throw new IllegalArgumentException();
      }
      id = productCode + "-" + iteration;
   }

   public SprintId(String id) {
      if (!id.matches("\\w{3}-\\d+")) {
         throw new IllegalArgumentException("Invalid format");
      }
      this.id = id;
   }

   public String productCode() {
      return id.split("-")[0];
   }

   public int iteration() {
      return Integer.parseInt(id.split("-")[1]);
   }
}
