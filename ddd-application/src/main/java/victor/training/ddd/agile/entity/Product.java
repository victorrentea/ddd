package victor.training.ddd.agile.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Product {
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
   private List<Sprint> sprints = new ArrayList<>();

   @OneToMany(mappedBy = "product")
   private List<Release> releases = new ArrayList<>();

   public Product() {
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

   public int getCurrentIteration() {
      return this.currentIteration;
   }

   public int getCurrentVersion() {
      return this.currentVersion;
   }

   public String getCode() {
      return this.code;
   }

   public String getName() {
      return this.name;
   }

   public String getOwnerEmail() {
      return this.ownerEmail;
   }

   public String getOwnerName() {
      return this.ownerName;
   }

   public String getOwnerPhone() {
      return this.ownerPhone;
   }

   public String getTeamMailingList() {
      return this.teamMailingList;
   }

   public List<Sprint> getSprints() {
      return this.sprints;
   }

   public List<Release> getReleases() {
      return this.releases;
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

   public Product setCode(String code) {
      this.code = code;
      return this;
   }

   public Product setName(String name) {
      this.name = name;
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
      return "Product(id=" + this.getId() + ", currentIteration=" + this.getCurrentIteration() + ", currentVersion=" + this.getCurrentVersion() + ", code=" + this.getCode() + ", name=" + this.getName() + ", ownerEmail=" + this.getOwnerEmail() + ", ownerName=" + this.getOwnerName() + ", ownerPhone=" + this.getOwnerPhone() + ", teamMailingList=" + this.getTeamMailingList() + ", sprints=" + this.getSprints() + ", releases=" + this.getReleases() + ")";
   }
}

