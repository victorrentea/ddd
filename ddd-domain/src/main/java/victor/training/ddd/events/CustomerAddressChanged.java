package victor.training.ddd.events;

import lombok.Value;

@Value
public class CustomerAddressChanged implements DomainEvent{
   long customerId;
}
