package victor.training.ddd.order.model;



// Value Object <> Entity

// Entity se poate schimba in timp "continuitity of change"
// Value Object cand se schimba --> apare unul nou. NU are sens conceptul de "o parte din VO" a
// fost schimbata, asa cum nici "o parte din 7 s-a modificat"

//class Money {
//   double amount;
//   Currency currency;
//}


import javax.validation.constraints.NotNull;
import java.util.Objects;


class Address { // VO
   private final String streetName;
   private final Integer streetNumber;
   private final String city;
   private final Integer postalCode;

   Address(String streetName, Integer streetNumber, String city, Integer postalCode) {
      this.streetName = Objects.requireNonNull(streetName);
      this.streetNumber = streetNumber;
      this.city = Objects.requireNonNull(city);
      this.postalCode = postalCode;
   }

   public Address withStreet(String streetName, int streetNumber) {
      return new Address(streetName, streetNumber, city, postalCode);
   }

   public String method(Person person) { // side effect free ::: NU modifica nimic
//      person.setCeva() // nu ai voie
//      this.personEntity.setCeva(); // VO refera Entitate - nu e bine
      return person.getName() + " de la " + city;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Address address = (Address) o;
      return streetNumber == address.streetNumber && postalCode == address.postalCode && Objects.equals(streetName, address.streetName) && Objects.equals(city, address.city);
   }

   @Override
   public int hashCode() {
      return Objects.hash(streetName, streetNumber, city, postalCode);
   }
}


//class PersonId {
//   private final String id;
//
//   public PersonId() {
//      id = UUID.randomUUID().toString();
//   }
//   public PersonId(String id) {
//      this.id = id;
//   }
//
//   public String getId() {
//      return id;
//   }
//}
// Aggregate
public class Person {
   private String id;
//   private PersonId id = new PersonId(); // 73
   private Address address ;

   private Long originCountryId;


   public Person(String id, Address address, Long originCountryId) {
      this.id = id;
      this.originCountryId = originCountryId;
      setAddress(address);
   }

   public void setAddress(Address address) {
      this.address = Objects.requireNonNull(address);
   }

   public Address getAddress() {
      return address;
   }

   {
      Person one = new Person("13", new Address("s", 12, "City", 12), 14L);
      Person two = new Person("13", new Address("XX", 12, "City", 12), 14L);

      one.setAddress(one.getAddress().withStreet("Alta",13));

      System.out.println(one.equals(two));

      // UC1 are de importat date despre persoane>> el va cauta persoanele existente (posibil diferite ca stare) dupa ID
//      filter(p -> p.id = idulCautat)
      // UC2 are de comparat daca din UI s-a facut vreo schimbare in starea unui Person>> va compara camp cu camp
//          din exterior ii compari campurile
      // ==> evita sa implementezi hashcode equals pe entitati
   }

   public String getName() {
      return null;
   }

//   @Override
//   public boolean equals(Object o) {
//      if (this == o) return true;
//      if (o == null || getClass() != o.getClass()) return false;
//      Person person = (Person) o;
//      return Objects.equals(id, person.id) && Objects.equals(address, person.address) && Objects.equals(originCountryId, person.originCountryId);
//   }
//
//   @Override
//   public int hashCode() {
//      return Objects.hash(id, address, originCountryId);
//   }
}







class Company {
      Address address;

   public void setAddress(Address address) {
//      if (address.country == "") throw
      this.address = address;
   }
   //   String id;
   //   Bio bio;
   //   List<String> phones;

   Long fiscalCountryId;
}
//Aggregate
class Country {

}