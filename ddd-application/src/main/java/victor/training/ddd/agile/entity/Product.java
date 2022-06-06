package victor.training.ddd.agile.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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

   private String ownerEmail;
   private String ownerName;
   private String ownerPhone;

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

   public int getCurrentIteration() {
      return currentIteration;
   }

   public int getCurrentVersion() {
      return currentVersion;
   }

   public String getCode() {
      return code;
   }

   public String getName() {
      return name;
   }

   public String getOwnerEmail() {
      return ownerEmail;
   }

   public String getOwnerName() {
      return ownerName;
   }

   public String getOwnerPhone() {
      return ownerPhone;
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

   public Product setId(Long id) {
      this.id = id;
      return this;
   }

   public Product setCurrentIteration(int currentIteration) {
      this.currentIteration = currentIteration;
      return this;
   }

   public Product setCurrentVersion(int currentVersion) {
      this.currentVersion = currentVersion;
      return this;
   }

   public Product setOwnerEmail(String ownerEmail) {
      this.ownerEmail = ownerEmail;
      return this;
   }

   public Product setOwnerName(String ownerName) {
      this.ownerName = ownerName;
      return this;
   }

   public Product setOwnerPhone(String ownerPhone) {
      this.ownerPhone = ownerPhone;
      return this;
   }

   public Product setTeamMailingList(String teamMailingList) {
      this.teamMailingList = teamMailingList;
      return this;
   }

   public Product setSprints(List<Sprint> sprints) {
      this.sprints = sprints;
      return this;
   }

   public Product setReleases(List<Release> releases) {
      this.releases = releases;
      return this;
   }


   public String toString() {
      return "Product(id=" + getId() + ")";
   }
}

