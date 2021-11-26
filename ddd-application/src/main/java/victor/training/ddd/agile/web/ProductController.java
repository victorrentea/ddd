package victor.training.ddd.agile.web;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.domain.entity.Product;
import victor.training.ddd.agile.domain.repo.ProductRepo;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {
   private final ProductRepo productRepo;


   @Data
   public static class ProductDto {
      public Long id;
      public String code;
      public String name;
      public String mailingList;
   }

   @PostMapping("products")
   public Long createProduct(@RequestBody ProductDto dto) {
      if (productRepo.existsByCode(dto.code)) {
         throw new IllegalArgumentException("Code already defined");
      }
      Product product = new Product(dto.code, dto.name, dto.mailingList);
      return productRepo.save(product).getId();
   }

   @GetMapping("products/{id}")
   public ProductDto getProduct(@PathVariable long id) {
      Product product = productRepo.findOneById(id);
      ProductDto dto = new ProductDto();
      dto.id = product.getId();
      dto.name = product.getName();
      dto.code = product.getCode();
      dto.mailingList = product.getMailingList();
      return dto;
   }

}
