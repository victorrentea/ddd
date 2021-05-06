package victor.training.ddd;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface AllChannels {
   @Output("ordersConfirmedOut")
   MessageChannel ordersConfirmedOut();

   @Input("ordersConfirmedIn")
   SubscribableChannel ordersConfirmedIn();
}
