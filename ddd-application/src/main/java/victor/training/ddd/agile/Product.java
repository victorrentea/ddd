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


// AGGREGATE = a set of objects ENTIT + VO
// AGGREGATE ROOT = one of those ENTITIES ("the parent") whose
// repons is to enforce the consistency of the entier AGGREGATE by encapsulating
// all changes to anything insidee the AGGREGATE THAT can lead to inconsistencies
@Getter
@Entity //= aggregate root
class Product {
   @Id
   @GeneratedValue
   private Long id;
   private int currentIteration = 0;
   private int currentVersion = 0;
   private String code;
   @NotNull
//   @Column(nullable = false) or NOT NULL in incrementals (flyway)
   private String name;

   @Embedded
   private Contact owner;
   private String teamMailingList;

   @OneToMany(mappedBy = "product")
   private List<BacklogItem> backlogItems = new ArrayList<>();
   @OneToMany(mappedBy = "product")
   private List<Sprint> sprints = new ArrayList<>();

   //private children
   @JoinColumn
   @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
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

   private int incrementAndGetVersion() {
      return ++ currentVersion;
   }

   public void addRelease(Sprint sprint, String releaseNotes) {
      Release release = new Release(sprint.getId(),
          releaseNotes,
          incrementAndGetVersion() + ".0");
      getReleases().add(release);
   }
}

interface ProductRepo extends CustomJpaRepository<Product, Long> {
   boolean existsByCode(String code);
}