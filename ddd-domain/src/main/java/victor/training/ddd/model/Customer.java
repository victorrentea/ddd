package victor.training.ddd.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import victor.training.ddd.events.CustomerAddressChanged;
import victor.training.ddd.events.DomainEvent;
import victor.training.ddd.events.DomainEventsPublisher;
import victor.training.ddd.model.User.UserId;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

import static java.util.Objects.requireNonNull;

@Entity
public class Customer extends AbstractAggregateRoot<Customer> {
   @Id
   @GeneratedValue
   private Long id;
   private String name;
   @Embedded
   private CustomerAddress address;
   private String email;

//@ManyToOne
//Site site2;
      private Long siteId;

   protected Customer() {}
   public Customer(String name, CustomerAddress address, String email, long siteId) {
      this.name = requireNonNull(name);
      this.address = address;
      this.email = requireNonNull(email);
      this.siteId = siteId;
   }

   public void setAddress(CustomerAddress newAddress) {
      if (this.address.equals(newAddress)) {
         return;
      }
      System.out.println("Update customer address");
      this.address = newAddress;

      CustomerAddressChanged event = new CustomerAddressChanged(id);
//      registerEvent(event);

      DomainEventsPublisher.publish(event);
   }

   @Getter
   private LocalDate creationDate = LocalDate.now();

   public Long getId() {
      return id;
   }

   public String name() {
      return name;
   }

   public String email() {
      return email;
   }
}
