package victor.training.ddd.customer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.AbstractAggregateRoot;
import victor.training.ddd.MyException;
import victor.training.ddd.MyException.ErrorCode;
import victor.training.ddd.common.events.CustomerAddressChanged;
import victor.training.ddd.common.events.DomainEventsPublisher;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

@ToString
@Entity
public class Customer extends AbstractAggregateRoot<Customer> {

   @EmbeddedId
   private CustomerId id;
   private String name;
   private String email;
   @Embedded
   private CustomerAddress address;

//   @ManyToOne
//   private Site site2;
   private Long siteId;

   @Getter
   private LocalDate creationDate = LocalDate.now();

   protected Customer() {
   }

   public Customer(CustomerId id, String name, String email, long siteId) {
      this.id = requireNonNull(id);
      setName(name);
      this.email = requireNonNull(email);
      this.siteId = siteId;
   }

   public void setName(String name) {
      if (name.trim().length() <= 5) {
         throw new MyException(ErrorCode.CUSTOMER_NAME_TOO_SHORT, name);
      }
      this.name = name;
   }

   public void setAddress(CustomerAddress /*= VO*/ newAddress) {
      if (this.address.equals(newAddress)) {
         return;
      }
      System.out.println("Update customer address");
      this.address = newAddress;

      CustomerAddressChanged event = new CustomerAddressChanged(id.value());
//      registerEvent(event);

      DomainEventsPublisher.publish(event);
      System.out.println("AFTER");
   }

   public CustomerId id() {
      return id;
   }

   public String name() {
      return name;
   }

   public String email() {
      return email;
   }

   @EqualsAndHashCode
   @Embeddable
   public static class CustomerId implements Serializable {
      @Column(name = "id")
      private Long value; // not final (for hibernate)

      private CustomerId() {
      } // the death toll for Hibernate // generates Unique from java

      public CustomerId(long value) {
         this.value = value;
      }

      public Long value() {
         return value;
      }
   }
}
