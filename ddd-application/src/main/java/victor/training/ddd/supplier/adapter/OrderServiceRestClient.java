package victor.training.ddd.supplier.adapter;

import victor.training.ddd.supplier.model.OrderVO;
import victor.training.ddd.supplier.service.OrderServiceClient;

public class OrderServiceRestClient implements OrderServiceClient {
   @Override
   public OrderVO getOrder(long orderId) {

      // TODO REST Teplate
      // TODO conversion
      return null;
   }
}
