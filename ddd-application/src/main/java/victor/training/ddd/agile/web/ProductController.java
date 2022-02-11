package victor.training.ddd.agile.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.agile.domain.repo.ProductRepo;
import victor.training.ddd.agile.web.dto.ProductDto;
import victor.training.ddd.agile.web.mapper.ProductMapper;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {
   private final ProductRepo productRepo;
   private final ProductMapper productMapper;

   @PostMapping("products")
   public Long createProduct(@RequestBody ProductDto dto) {
      if (productRepo.existsByCode(dto.code)) { // UQ will crash anyway
         throw new IllegalArgumentException("Code already defined");
      }
      Product product = productMapper.toEntity(dto);
      return productRepo.save(product).getId();
   }

   @GetMapping("products/{id}")
   public ProductDto getProduct(@PathVariable long id) {
      Product product = productRepo.findOneById(id);
      return new ProductDto(product);
   }
}
