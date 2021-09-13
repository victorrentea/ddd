package victor.training.ddd.order.model;

import lombok.Value;

@Value
public class PriceRequestedEvent {
   String productId;
}
