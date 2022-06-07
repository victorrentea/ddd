package victor.training.ddd.agile.order.aggregates;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ProductToBuy {
   @Id
   @GeneratedValue
   private Long id;

   private String name;

   @Lob
   private String description;

   private int shippingDaysEst;
   private int shippingCost;
   private boolean shippingToEasyBox;
   private String shippingProvider;
   private boolean shippingViaRegularPostOption;

   private boolean returnable;
   private int returnMaxDays;

   @Enumerated(EnumType.STRING)
   private ProductCategory category;

   @ManyToOne
   private Supplier supplier;

}
