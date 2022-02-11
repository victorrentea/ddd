package victor.training.ddd.agile.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.agile.domain.repo.ProductRepo;
import victor.training.ddd.agile.web.dto.ProductDto;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController /*implements OpenApigeneratedApi*/ {
   private final ProductRepo productRepo;

   @PostMapping("products")
   public Long createProduct(@RequestBody ProductDto dto) {
      Product product = dto.toEntity();
      if (productRepo.existsByCode(product.getCode())) { // UQ will crash anyway
         throw new IllegalArgumentException("Code already defined");
      }
      // tomorrow, more logic ===> Move to a Domain Service in the other maven module
      return productRepo.save(product).getId();
   }

   @GetMapping("products/{id}")
   public ProductDto getProduct(@PathVariable long id) {
      return new ProductDto(productRepo.findOneById(id));
   }
}
