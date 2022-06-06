package victor.training.ddd.agile.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
class ProductOwner {
   @Id @GeneratedValue
   private Long id;

   String ownerEmail;
   String ownerName;
   String ownerPhone;
}

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "UK_CODE", columnNames = "CODE")) // if on a relational DB, it's an opportunity hard to redfus
// BUT ! this does NOT mean that you allow others to write directly to your DATABASE. That would be a reason to sue them. (void the warranty)
public class Product { // PRODUCT
   @Id
   @GeneratedValue
   private Long id;
   private int currentIteration = 0;
   private int currentVersion = 0;
   private String code;
   @Column(nullable = false)
   private String name;

   @OneToOne(cascade = CascadeType.ALL)
   private ProductOwner owner = new ProductOwner();

//   private String ownerEmail;
//   private String ownerName;
//   private String ownerPhone;

   private String teamMailingList;

   @OneToMany(mappedBy = "product")
   private List<Sprint> sprints = new ArrayList<>();

   @OneToMany(mappedBy = "product")
   private List<Release> releases = new ArrayList<>();

   protected Product() { // only by hibernate should never be called by developers
   }
   public Product(String code, String name) {
      this.code = Objects.requireNonNull(code);
      this.name = Objects.requireNonNull(name.trim());
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

   public String getTeamMailingList() {
      return teamMailingList;
   }

   public List<Sprint> getSprints() {
      return sprints;
   }

   public List<Release> getReleases() {
      return releases;
   }

   public Product setOwnerEmail(String ownerEmail) {
      this.owner.ownerEmail = ownerEmail;
      return this;
   }

   public Product setOwnerName(String ownerName) {
      this.owner.ownerName = ownerName;
      return this;
   }

   public Product setOwnerPhone(String ownerPhone) {
      this.owner.ownerPhone = ownerPhone;
      return this;
   }

   public Product setTeamMailingList(String teamMailingList) {
      this.teamMailingList = teamMailingList;
      return this;
   }

   public String toString() {
      return "Product(id=" + getId() + ")";
   }
}

