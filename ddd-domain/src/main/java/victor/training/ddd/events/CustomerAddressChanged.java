package victor.training.ddd.events;

public class CustomerAddressChanged implements DomainEvent{
   private final long customerId;

   public CustomerAddressChanged(long customerId) {
      this.customerId = customerId;
   }

   public long getCustomerId() {
      return customerId;
   }
}
