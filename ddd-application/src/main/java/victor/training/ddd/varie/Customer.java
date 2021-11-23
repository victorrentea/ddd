package victor.training.ddd.varie;

import java.util.Objects;

public class Customer {
//   private String firstName;
//   private String middleName;
//   private String lastName;
   private FullName fullName;
   private String a;
   private String b;
   private String c;

   public Customer(FullName fullName, String a, String b, String c) {
      this.fullName = Objects.requireNonNull(fullName);
      this.a = a;
      this.b = b;
      this.c = c;
   }
}

class FullName {
   private String firstName;
   private String middleName;
   private String lastName;

   public FullName(String firstName, String middleName, String lastName) {
      this.firstName = Objects.requireNonNull(firstName);
      this.middleName = middleName;
      this.lastName = Objects.requireNonNull(lastName);
   }
}

class Address {
   private final String city;
   private final String streetName;
   private final Integer streetNumber;

   Address(String city, String streetName, Integer streetNumber) {
      this.city = Objects.requireNonNull(city);
      if (streetNumber != null && streetName == null) {
         throw new IllegalArgumentException();
      }
      this.streetName = streetName;
      this.streetNumber = streetNumber;
   }

//   public Address withStreetAddress(String newStreetName, Integer newStreetNumber) {
//      return new Address(city, newStreetName, newStreetNumber);
//   }
//
//   {
//      new Address("Rasi", "Centrala", 44)
//          .withStreetAddress(null, null)
////          .withStreetName(null) // crapa
////          .withStreetNumber(null)
//      ;
//   }
}