package victor.training.ddd.agile.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.application.dto.ProductDto;
import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.agile.domain.model.ProductOwner;
import victor.training.ddd.agile.domain.repo.ProductRepo;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductService {
   private final ProductRepo productRepo;



   @PostMapping("products")
   public Long createProduct(@RequestBody ProductDto dto) {
      if (productRepo.existsByCode(dto.code)) {
         throw new IllegalArgumentException("Code already defined");
      }
      ProductOwner po = new ProductOwner(dto.poEmail, dto.poName, dto.poPhone);
      Product product = new Product(dto.code, dto.name, dto.mailingList, po);
      return productRepo.save(product).getId();
   }

   @GetMapping("products/{id}")
   public ProductDto getProduct(@PathVariable long id) {
      Product product = productRepo.findOneById(id);
      ProductDto dto = new ProductDto();
      dto.id = product.getId();
      dto.name = product.getName();
      dto.code = product.getCode();
      dto.mailingList = product.getTeamMailingList();
      return dto;
   }
}
