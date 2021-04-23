package victor.training.ddd.supplier.service;

import victor.training.ddd.supplier.model.OrderVO;

public interface OrderServiceClient {
   OrderVO getOrder(long orderId);
}
