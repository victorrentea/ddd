package victor.training.ddd.agile.domain.model;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


// AGGREGATE = a set of objects ENTIT + VO
// AGGREGATE ROOT = one of those ENTITIES ("the parent") whose
// repons is to enforce the consistency of the entier AGGREGATE by encapsulating
// all changes to anything insidee the AGGREGATE THAT can lead to inconsistencies
@Getter
@Entity //= aggregate root
public class Product {
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

   public Release addRelease(Sprint sprint, String releaseNotes) {
      Release release = new Release(sprint.getId(),
          releaseNotes,
          incrementAndGetVersion() + ".0");
      getReleases().add(release);
      return release;
   }
}

