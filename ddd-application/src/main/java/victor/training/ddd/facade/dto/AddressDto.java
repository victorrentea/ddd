package victor.training.ddd.facade.dto;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class AddressDto {
   @NotNull
   public String streetName;
   public Integer streetNumber;

   @NotNull
   public String city;

   public Integer postalCode;

   @AssertTrue
   public boolean isValid() {
      if (!city.equalsIgnoreCase("BUCHAREST")) {
         return postalCode != null;
      }
      return true;
   }

}


