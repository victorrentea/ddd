package victor.training.ddd.agile.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.application.dto.ProductDto;
import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.agile.domain.model.ProductOwner;
import victor.training.ddd.agile.domain.repo.ProductRepo;
import victor.training.ddd.common.DDD.ApplicationService;

import javax.validation.Valid;

@Slf4j
@RestController
@ApplicationService
@RequiredArgsConstructor
public class ProductFacade {
   private final ProductRepo productRepo;

//@PreAuthorized("hasRole('ADMIN')") < forgetting to put this = the most common security hole. ie. securing only the visible Frontend element
   @PostMapping("products")
   public Long createProduct(@RequestBody @Valid ProductDto dto) {
      if (productRepo.existsByCode(dto.code)) {
         throw new IllegalArgumentException("Code already defined");
      }
      ProductOwner po = new ProductOwner(dto.poEmail, dto.poName, dto.poPhone);
      Product product = new Product(dto.code, dto.name, po)
          .setTeamMailingList(dto.mailingList);
      return productRepo.save(product).getId();
   }

   @GetMapping("products/{id}")
   public ProductDto getProduct(@PathVariable long id) {
      return new ProductDto(productRepo.findOneById(id));
   }
}
