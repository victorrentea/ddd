package victor.training.ddd.agile;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.common.repo.CustomJpaRepository;

import javax.persistence.*;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

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
      dto.mailingList = product.getMailingList();
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
   private String mailingList;

   // TODO extract @Embeddable
   private String ownerEmail;
   private String ownerName;
   private String ownerPhone;
   private String teamMailingList;


//   @Version
//   private Long version;
//
//   public void markAsDirty() {
//      version ++;
//   }


   @OneToMany
   @JoinColumn
   private List<Release> releases = new ArrayList<>();

   protected Product() { // doar pt hibernate
   }
   public Product(String code, String name, String mailingList) {
//      if (Objects.requireNonNull(code,"code is required").length() != 3) {
//         throw new IllegalArgumentException("code size should be 3");
//      }
      this.code = code;
      this.name = name;
      this.mailingList = mailingList;

      getValidator().validate(this);
   }

   private Validator getValidator() {
      return Validation.buildDefaultValidatorFactory().getValidator();
   }

   public int nextIteration() {
      return ++ currentIteration;
   }

   private String nextReleaseVersion() {
      return ( ++ currentVersion) + ".0";
   }

   public Release createRelease(int iteration, BacklogItemRepo backlogItemRepo) {
      int lastReleasedIteration = releases.stream()
          .mapToInt(Release::getSprintIteration)
          .max()
          .orElse(0);

      List<BacklogItem> releasedItems = backlogItemRepo.findDoneItemsBetweenIterations(lastReleasedIteration + 1, iteration);

      String releaseNotes = releasedItems.stream().map(BacklogItem::getTitle).collect(joining("\n"));

      Release release = new Release(nextReleaseVersion() ,iteration, releaseNotes );
      releases.add(release);
      return release;
   }
}

interface ProductRepo extends CustomJpaRepository<Product, Long> {
   boolean existsByCode(String code);
}