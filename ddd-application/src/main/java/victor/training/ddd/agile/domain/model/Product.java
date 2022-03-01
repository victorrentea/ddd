package victor.training.ddd.agile.domain.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

// consider encapsulating changes
@Entity
// AggregateRoot
public class Product {
   @Id
   @GeneratedValue
   private Long id;
   private int currentIteration = 0;
   private int currentVersion = 0;

   @NotNull // automatically checked by Hibernate on perist/merge
   @Min(3)
   // havily used in practice > 60-80%
   // relying on annotations to check validity of entities allows
   // a "window of inconsistency" until you save or the Tx ends

   @Column(nullable = false) // UPDATE SET code = null
//   @UniqueCode
   private String code;
   @Column(nullable = false) // alter table set name not null;
   private String name;

   @Embedded
   private ProductOwner owner;

   private String teamMailingList;

   @OneToMany(mappedBy = "product")
   private List<Release> releases = new ArrayList<>();

   private Product() {
   }

   public Product(String code, String name, ProductOwner owner) {
//      HocusPocus.getValidator().validate(this); // still using validation annotations
      if (code.length() != 3) throw new RuntimeException("Boom");
      this.code = requireNonNull(code);
      setName(name);
      setOwner(owner);
   }

   public Product setTeamMailingList(String teamMailingList) {
      this.teamMailingList = teamMailingList;
      return this;
   }

   public Product setName(String name) {
      this.name = requireNonNull(name);
//      HocusPocus.getValidator().validate(this);
      return this;
   }

   public Product setOwner(ProductOwner owner) {
      this.owner = owner;
      return this;
   }
   public Optional<String> getTeamMailingList() {
      return ofNullable(teamMailingList);
   }

   public int incrementAndGetIteration() {
      currentIteration++;
      return currentIteration;
   }

   public int incrementAndGetVersion() {
      currentVersion++;
      return currentVersion;
   }

   public Long getId() {
      return id;
   }

   public String getCode() {
      return code;
   }

   public String getName() {
      return name;
   }

   public ProductOwner getOwner() {
      return owner;
   }

   public List<Release> getReleases() {
      return releases;
   }

}

