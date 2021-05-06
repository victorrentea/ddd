package victor.training.ddd.order.service;

public interface OrderEventsSender {
   void sendOrderConfirmed(Long orderId);
}
