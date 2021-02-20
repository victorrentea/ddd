package victor.training.ddd.model;

import lombok.*;

import javax.persistence.Embeddable;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // hibernate
@Embeddable
public class CustomerAddress {
   private Long countryId;
   private String city;
   private String streetAddress;

   public CustomerAddress(Long countryId, String city, String streetAddress) {
      this.countryId = countryId;
      this.city = city;
      this.streetAddress = streetAddress;
   }
}
