package victor.training.ddd.order.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import victor.training.ddd.AllChannels;
import victor.training.ddd.order.service.OrderEventsSender;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventsMessageSender implements OrderEventsSender {
   private final AllChannels allChannels;
   @Override
   public void sendOrderConfirmed(Long orderId) {
      allChannels.ordersConfirmedOut().send(MessageBuilder.withPayload(orderId + "").build());
   }
}
