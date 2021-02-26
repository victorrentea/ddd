package victor.training.ddd.facade.dto;

import victor.training.ddd.customer.model.Customer;

import java.text.SimpleDateFormat;

public class CustomerDto { // is this an abused Dto ?
   public Long id;
   public String name;
   public String email;
   public Long siteId;
   public String creationDateStr;

public CustomerDto() {}
   public CustomerDto(Customer customer) {
      name = customer.name();
      email = customer.email();
      creationDateStr = new SimpleDateFormat("yyyy-MM-dd").format(customer.getCreationDate());
      id = customer.id().value();
   }

   public CustomerDto(String name, String email) {
      this.name = name;
      this.email = email;
   }

//   public Customer toEntity() {
   // impossible because I need a dep to Sprin Bean
   // could go to a @Component CustomerMapper class next to you
//      return new Customer(id, name, "a@b.com", siteId);
//   }

}
