package victor.training.ddd.agile;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.common.repo.CustomJpaRepository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
class ProductService {
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
          .setOwnerPhone(dto.poPhone)
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




@Data
@Entity
class Product {
   @Id
   @GeneratedValue
   private Long id;
   private int currentIteration = 0;
   private int currentVersion = 0;
   private String code;
   private String name;

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

   public int incrementAndGetIteration() {
      currentIteration++;
      return currentIteration;
   }

   public int incrementAndGetVersion() {
      currentVersion++;
      return currentVersion;
   }
}

interface ProductRepo extends CustomJpaRepository<Product, Long> {
   boolean existsByCode(String code);
}