package victor.training.ddd.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class SupplierId {
   @Column(name="SUPPLIER_ID")
   public String value;

}
