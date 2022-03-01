package victor.training.ddd.agile.domain.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// consider encapsulating changes
@Entity
// AggregateRoot
public class Product {
   @Id
   @GeneratedValue
   private Long id;
   private int currentIteration = 0;
   private int currentVersion = 0;

//   @NotNull // useless
   @Column(nullable = false) // UPDATE SET code = null
   private String code;
   @Column(nullable = false) // alter table set name not null;
   private String name;

   @Embedded
   private ProductOwner owner;

   private String teamMailingList;

//   @OneToMany
//   @JoinColumn
//   private List<BacklogItem> backlogItems = new ArrayList<>();
//   @OneToMany(mappedBy = "product")
//   private List<Sprint> sprints = new ArrayList<>();
   @OneToMany(mappedBy = "product")
   private List<Release> releases = new ArrayList<>();

   public Product(String code, String name, ProductOwner owner) {
      this.code = Objects.requireNonNull(code);
      this.name = Objects.requireNonNull(name);
      this.owner = Objects.requireNonNull(owner);
   }

   private Product() {
   }

   public Product setTeamMailingList(String teamMailingList) {
      this.teamMailingList = teamMailingList;
      return this;
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
      return this.id;
   }

   public @NotNull String getCode() {
      return this.code;
   }

   public String getName() {
      return this.name;
   }

   public ProductOwner getOwner() {
      return this.owner;
   }

   public Optional<String> getTeamMailingList() {
      return Optional.ofNullable(this.teamMailingList);
   }

   public List<Release> getReleases() {
      return this.releases;
   }

   public Product setId(Long id) {
      this.id = id;
      return this;
   }

   public Product setName(String name) {
      this.name = name;
      return this;
   }

   public Product setOwner(ProductOwner owner) {
      this.owner = owner;
      return this;
   }

}

