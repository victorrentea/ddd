package victor.training.ddd.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import victor.training.ddd.events.CustomerAddressChanged;
import victor.training.ddd.events.DomainEventsPublisher;

import javax.persistence.*;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

@ToString
@Entity
public class Customer extends AbstractAggregateRoot<Customer> {

   @EqualsAndHashCode
   @Embeddable
   public static class CustomerId implements Serializable {
      private Long value;
      protected CustomerId() {} // generates Unique from java
      public CustomerId(Long value) {
         this.value = value;
      }
      public Long value() {
         return value;
      }
   }

   @EmbeddedId
   private CustomerId id;
   private String name;
   private String email;
   @Embedded
   private CustomerAddress address;

   //@ManyToOne
//Site site2;
      private Long siteId;

   protected Customer() {}
   public Customer(CustomerId id, String name, CustomerAddress address, String email, long siteId) {
      this.id = requireNonNull(id);
      this.name = requireNonNull(name);
      this.address = address;
      this.email = requireNonNull(email);
      this.siteId = siteId;
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
   }

   @Getter
   private LocalDate creationDate = LocalDate.now();

   public CustomerId id() {
      return id;
   }

   public String name() {
      return name;
   }

   public String email() {
      return email;
   }
}
