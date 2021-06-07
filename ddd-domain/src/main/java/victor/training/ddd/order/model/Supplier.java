package victor.training.ddd.order.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Supplier {
   @EmbeddedId
   private SupplierId id;
   private String name;

}
