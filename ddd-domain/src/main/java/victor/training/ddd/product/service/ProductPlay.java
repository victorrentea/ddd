package victor.training.ddd.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import victor.training.ddd.product.model.Product;
import victor.training.ddd.product.model.ProductCategory;
import victor.training.ddd.product.model.ShippingDetails;
import victor.training.ddd.product.repo.ProductRepo;

@Component
@RequiredArgsConstructor
public class ProductPlay implements CommandLineRunner {
   private final ProductRepo productRepo;

   @Override
   public void run(String... args) throws Exception {
      ShippingDetails shipping = new ShippingDetails(4, "SameDay");
      productRepo.save(new Product("nume", shipping, 15, ProductCategory.BAB));
   }
}
