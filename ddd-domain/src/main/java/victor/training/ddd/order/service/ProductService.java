package victor.training.ddd.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.ddd.order.model.PriceRequestedEvent;
import victor.training.ddd.order.model.Product;
import victor.training.ddd.order.repo.ProductRepo;

@Service
@RequiredArgsConstructor
public class ProductService {
   private final ProductRepo productRepo;
   @EventListener
   public void method(PriceRequestedEvent event) {
      Product product = productRepo.findById(event.getProductId()).get();
      product.reportPriceQueried();
      productRepo.save(product);
   }
}
