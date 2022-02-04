package victor.training.ddd.agile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.ProductController.ProductDto;
import victor.training.ddd.common.repo.CustomJpaRepository;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Retention(RetentionPolicy.RUNTIME)
@Component
@interface Mapper {

}

@Mapper
class ProductMapper {
//   Repo repo;
   public Product toEntity(ProductDto dto) { // noble goal to keep mapping static < doable if DDD more aggresively
      Product product = new Product(
          dto.code,
          dto.name,
          dto.mailingList); // in readlity have 10 params constructor
      // BREAK THE MODEL FURHTER. MORE VALUE OBJECT 10 > 7
      return product;
   }
}
@Slf4j
@RestController
@RequiredArgsConstructor
class ProductController {
   private final ProductRepo productRepo;
   private final ProductMapper productMapper;


   @Data
   static class ProductDto {
      public Long id;
      public String code;
      public String name;
      public String mailingList;
   }

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

      ProductDto dto = new ProductDto();
      dto.id = product.getId();
      dto.name = product.getName();
      dto.code = product.getCode();
      dto.mailingList = product.getTeamMailingList();
      return dto;
   }

}


@AllArgsConstructor
@Getter
@Embeddable
class Contact {
   private String email;
   private String name;
   private String phone;
}


@Getter
@Entity
class Product {
   @Id
   @GeneratedValue
   private Long id;
   private int currentIteration = 0;
   private int currentVersion = 0;
   private String code;
   @NotNull
   private String name;

   @Embedded
   private Contact owner;
//   private String ownerEmail;
//   private String ownerName;
//   private String ownerPhone;
   private String teamMailingList;

   @OneToMany(mappedBy = "product")
   private List<BacklogItem> backlogItems = new ArrayList<>();
   @OneToMany(mappedBy = "product")
   private List<Sprint> sprints = new ArrayList<>();
   @OneToMany(mappedBy = "product")
   private List<Release> releases = new ArrayList<>();

   private Product() {} // just for hinernate, not needed in playout
   public Product(String code, String name, String teamMailingList) {
      setCode(code);
      this.name = Objects.requireNonNull(name);
      this.teamMailingList = Objects.requireNonNull(teamMailingList);
   }

   public Product setCode(String code) {
      this.code = Objects.requireNonNull(code);
      return this;
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