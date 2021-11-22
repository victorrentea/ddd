package victor.training.ddd.product.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
public class ShippingDetails {
   private Integer shippingDaysEst; // REQUIRED
   private Integer shippingCost;
   private Boolean shippingToEasyBox;
   private String shippingProvider; // REQUIRED
   private Boolean shippingViaRegularPostOption;

   protected ShippingDetails() { // for Hibernate only
   }

   public ShippingDetails(int shippingDaysEst, String shippingProvider) {
      this.shippingDaysEst = shippingDaysEst;
      this.shippingProvider = Objects.requireNonNull(shippingProvider);
   }

}