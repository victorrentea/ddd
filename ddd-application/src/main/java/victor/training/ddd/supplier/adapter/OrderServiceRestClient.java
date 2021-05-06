package victor.training.ddd.supplier.adapter;

import org.springframework.stereotype.Service;
import victor.training.ddd.supplier.model.OrderVO;
import victor.training.ddd.supplier.service.OrderServiceClient;

@Service
public class OrderServiceRestClient implements OrderServiceClient {
   @Override
   public OrderVO getOrder(long orderId) {

      // TODO REST Template
      // TODO conversion
      return null;
   }
}
