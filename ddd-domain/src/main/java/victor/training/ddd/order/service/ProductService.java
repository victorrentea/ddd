package victor.training.ddd.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.ddd.order.model.CustomerId;
import victor.training.ddd.order.repo.ProductRepo;

@Service
@RequiredArgsConstructor
public class ProductService {
   private final ProductRepo productRepo;

   public void caller(String orderId, CustomerId customerid, String productId) {
      met( customerid, orderId, productId);
   }
   public void met(CustomerId customerId, String orderId, String productId) {

   }
}
