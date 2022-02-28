package victor.training.ddd.agile;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductService {
   private final ProductRepo productRepo;

   @Data
   static class ProductDto {
      public Long id;
      public String code;
      public String name;
      public String mailingList;
      public String poEmail;
      public String poName;
      public String poPhone;
   }

   @PostMapping("products")
   public Long createProduct(@RequestBody ProductDto dto) {
      if (productRepo.existsByCode(dto.code)) {
         throw new IllegalArgumentException("Code already defined");
      }
      Product product = new Product()
          .setCode(dto.code)
          .setName(dto.name)
          .setTeamMailingList(dto.mailingList)
          .setOwnerEmail(dto.poEmail)
          .setOwnerName(dto.poName)
          .setOwnerPhone(dto.poPhone);
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
