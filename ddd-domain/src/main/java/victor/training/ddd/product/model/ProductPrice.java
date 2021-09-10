package victor.training.ddd.product.model;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

public class ProductPrice {
   private BigDecimal romaniaPrice;
   private BigDecimal italyPrice;
   private BigDecimal serbiaPrice;

   ProductPrice() {}

   public ProductPrice(BigDecimal romaniaPrice, BigDecimal italyPrice, BigDecimal serbiaPrice) {
      this.romaniaPrice = romaniaPrice;
      this.italyPrice = italyPrice;
      this.serbiaPrice = requireNonNull(serbiaPrice);
   }

   public BigDecimal italyPrice() {
      return italyPrice;
   }

   public BigDecimal romaniaPrice() {
      return romaniaPrice;
   }

   public BigDecimal serbiaPrice() {
      return serbiaPrice;
   }
}
