package victor.training.ddd.order.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import java.util.Objects;

//@Embeddable
@Getter
@EqualsAndHashCode
public class ShippingDetails {
   @Column(nullable = false)
   private Integer shippingDaysEst; // REQUIRED
   private Integer shippingCost;
   private String shippingProvider; // REQUIRED
   private Boolean shippingToEasyBox;
   private Boolean shippingViaRegularPostOption;

//   @ManyToOne ShippingProvider shippingProvider; // de evitat
//   public void complicat() {
//      shippingProvider.setCeva();
//   }
//   getShippingProvider()

   protected ShippingDetails() { // for Hibernate only
   }

   public ShippingDetails(Integer shippingDaysEst, Integer shippingCost, Boolean shippingToEasyBox, String shippingProvider, Boolean shippingViaRegularPostOption) {
      if (shippingDaysEst < 0) {
         throw new IllegalArgumentException("Cannot ship in negative days!");
      }
      this.shippingDaysEst = Objects.requireNonNull(shippingDaysEst);
      // si a doua zi, un DBA face UPDATE PRODUCT SET SHIPPING_DAYS_EST = null
      this.shippingCost = shippingCost;
      this.shippingToEasyBox = shippingToEasyBox;
      this.shippingProvider = shippingProvider;
      this.shippingViaRegularPostOption = shippingViaRegularPostOption;
   }

   public ShippingDetails(int shippingDaysEst, String shippingProvider) {
      this(shippingDaysEst, null, null, shippingProvider, null);
   }

   public ShippingDetails withShippingCost(Integer newShippingCost) {
      return new ShippingDetails(shippingDaysEst, newShippingCost, shippingToEasyBox, shippingProvider, shippingViaRegularPostOption);
   }
}
