package victor.training.ddd.agile.domain.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
// AggregateRoot
public class Product {
   @Id
   @GeneratedValue
   private Long id;
   private int currentIteration = 0;
   private int currentVersion = 0;
   private String code;
   private String name;

   @Embedded
   @AttributeOverride(name = "name", column = @Column(name = "OWNER_NAME"))
   @AttributeOverride(name = "email", column = @Column(name = "OWNER_EMAIL"))
   @AttributeOverride(name = "phone", column = @Column(name = "OWNER_PHONE"))
   private ProductOwner owner;

   private String teamMailingList;

//   @OneToMany
//   @JoinColumn
//   private List<BacklogItem> backlogItems = new ArrayList<>();
//   @OneToMany(mappedBy = "product")
//   private List<Sprint> sprints = new ArrayList<>();
   @OneToMany(mappedBy = "product")
   private List<Release> releases = new ArrayList<>();

   public Product(String code, String name, String teamMailingList, ProductOwner owner) {
      this.code = code;
      this.name = name;
      this.teamMailingList = teamMailingList;
      this.owner = owner;
   }

   public int incrementAndGetIteration() {
      currentIteration++;
      return currentIteration;
   }

   public int incrementAndGetVersion() {
      currentVersion++;
      return currentVersion;
   }
}

