package victor.training.ddd.model;


import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class CustomerAddress {
   private Long countryId;
   private String city;
   private String streetAddress;

   protected CustomerAddress() {} // hibernate
   public CustomerAddress(Long countryId, String city, String streetAddress) {
      this.countryId = countryId;
      this.city = city;
      this.streetAddress = streetAddress;
   }

   public Long countryId() {
      return countryId;
   }

   public String city() {
      return city;
   }

   public String streetAddress() {
      return streetAddress;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CustomerAddress that = (CustomerAddress) o;
      return Objects.equals(countryId, that.countryId) &&
             Objects.equals(city, that.city) &&
             Objects.equals(streetAddress, that.streetAddress);
   }

   @Override
   public int hashCode() {
      return Objects.hash(countryId, city, streetAddress);
   }
}
