package victor.training.ddd.agile;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.common.repo.CustomJpaRepository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
class ProductController {
   private final ProductRepo productRepo;


   @Data
   static class ProductDto {
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
      dto.mailingList = product.getTeamMailingList();
      return dto;
   }

}




@Data
@Entity
class Product {
   @Id
   @GeneratedValue
   private Long id;
   private int currentIteration = 0;
   private int currentVersion = 0;
   @NotNull
   @Length(min = 3, max = 3)
   private String code;
   @NotNull
   private String name;
   @NotNull
   private final String mailingList;

   // TODO extract @Embeddable
   private String ownerEmail;
   private String ownerName;
   private String ownerPhone;
   private String teamMailingList;


   @OneToMany(mappedBy = "product")
   private List<BacklogItem> backlogItems = new ArrayList<>();
   @OneToMany(mappedBy = "product")
   private List<Sprint> sprints = new ArrayList<>();
   @OneToMany(mappedBy = "product")
   private List<Release> releases = new ArrayList<>();

   public Product(String code, String name, String mailingList) {
//      if (Objects.requireNonNull(code,"code is required").length() != 3) {
//         throw new IllegalArgumentException("code size should be 3");
//      }
      this.code = code;
      this.name = name;
      this.mailingList = mailingList;

      hocusPocus().validate(this);
   }

   private Validator hocusPocus() {
      return Validation.buildDefaultValidatorFactory().getValidator();
   }

   public int incrementAndGetIteration() {
      return ++ currentIteration;
   }

   public int incrementAndGetVersion() {
      return ++ currentVersion;
   }
}

interface ProductRepo extends CustomJpaRepository<Product, Long> {
   boolean existsByCode(String code);
}