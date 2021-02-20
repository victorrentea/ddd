package victor.training.ddd.model;


import javax.persistence.Embeddable;

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
}
