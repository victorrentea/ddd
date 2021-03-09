package victor.training.ddd.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;

@Data
@Embeddable
@AllArgsConstructor
public class SupplierId {
   public String value;
}
