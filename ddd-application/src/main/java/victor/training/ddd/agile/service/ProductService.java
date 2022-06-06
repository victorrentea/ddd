package victor.training.ddd.agile.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.entity.Product;
import victor.training.ddd.agile.dto.ProductDto;
import victor.training.ddd.agile.entity.ProductOwner;
import victor.training.ddd.agile.repo.ProductRepo;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductService {
   private final ProductRepo productRepo;

   @PostMapping("products")
   public Long createProduct(@RequestBody @Validated ProductDto dto) {
      if (productRepo.existsByCode(dto.code)) {
         throw new IllegalArgumentException("Code already defined");
      }
//      ProductOwner productOwner = new ProductOwner(dto.poName, dto.poEmail, dto.poPhone);
      Product product = new Product(dto.code, dto.name)
           .setTeamMailingList(dto.mailingList)
           .setOwnerUserid(dto.poUserid);
           ;
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
