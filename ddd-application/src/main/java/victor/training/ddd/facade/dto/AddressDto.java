package victor.training.ddd.facade.dto;

import javax.validation.constraints.NotNull;

public class AddressDto {
   @NotNull
   public String streetName;
   public Integer streetNumber;
   @NotNull
   public String city;

   public Integer postalCode;

//   @Valid
//   public boolean isValid() {
//      if (!city.equalsIgnoreCase("BUCHAREST")) {
//         Objects.requireNonNull(postalCode);
//      }
//   }

}


