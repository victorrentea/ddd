package victor.training.ddd.agile.entity;

import victor.training.ddd.agile.common.DDD.AggregateRoot;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;


@Entity
@AggregateRoot
public class Product {
   @Id
   @GeneratedValue
   private Long id;
   private int currentIteration = 0;
   private int currentVersion = 0;
   private String code;
   private String name;


   // Extract Value Object = Distill your domain (take the garbage out)
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

   public boolean equals(final Object o) {
      if (o == this) return true;
      if (!(o instanceof Product)) return false;
      final Product other = (Product) o;
      if (!other.canEqual((Object) this)) return false;
      final Object this$id = this.getId();
      final Object other$id = other.getId();
      if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
      if (this.getCurrentIteration() != other.getCurrentIteration()) return false;
      if (this.getCurrentVersion() != other.getCurrentVersion()) return false;
      final Object this$code = this.getCode();
      final Object other$code = other.getCode();
      if (this$code == null ? other$code != null : !this$code.equals(other$code)) return false;
      final Object this$name = this.getName();
      final Object other$name = other.getName();
      if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
      final Object this$ownerEmail = this.getOwnerEmail();
      final Object other$ownerEmail = other.getOwnerEmail();
      if (this$ownerEmail == null ? other$ownerEmail != null : !this$ownerEmail.equals(other$ownerEmail)) return false;
      final Object this$ownerName = this.getOwnerName();
      final Object other$ownerName = other.getOwnerName();
      if (this$ownerName == null ? other$ownerName != null : !this$ownerName.equals(other$ownerName)) return false;
      final Object this$ownerPhone = this.getOwnerPhone();
      final Object other$ownerPhone = other.getOwnerPhone();
      if (this$ownerPhone == null ? other$ownerPhone != null : !this$ownerPhone.equals(other$ownerPhone)) return false;
      final Object this$teamMailingList = this.getTeamMailingList();
      final Object other$teamMailingList = other.getTeamMailingList();
      if (this$teamMailingList == null ? other$teamMailingList != null : !this$teamMailingList.equals(other$teamMailingList))
         return false;
      final Object this$sprints = this.getSprints();
      final Object other$sprints = other.getSprints();
      if (this$sprints == null ? other$sprints != null : !this$sprints.equals(other$sprints)) return false;
      final Object this$releases = this.getReleases();
      final Object other$releases = other.getReleases();
      if (this$releases == null ? other$releases != null : !this$releases.equals(other$releases)) return false;
      return true;
   }

   protected boolean canEqual(final Object other) {
      return other instanceof Product;
   }

   public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      final Object $id = this.getId();
      result = result * PRIME + ($id == null ? 43 : $id.hashCode());
      result = result * PRIME + this.getCurrentIteration();
      result = result * PRIME + this.getCurrentVersion();
      final Object $code = this.getCode();
      result = result * PRIME + ($code == null ? 43 : $code.hashCode());
      final Object $name = this.getName();
      result = result * PRIME + ($name == null ? 43 : $name.hashCode());
      final Object $ownerEmail = this.getOwnerEmail();
      result = result * PRIME + ($ownerEmail == null ? 43 : $ownerEmail.hashCode());
      final Object $ownerName = this.getOwnerName();
      result = result * PRIME + ($ownerName == null ? 43 : $ownerName.hashCode());
      final Object $ownerPhone = this.getOwnerPhone();
      result = result * PRIME + ($ownerPhone == null ? 43 : $ownerPhone.hashCode());
      final Object $teamMailingList = this.getTeamMailingList();
      result = result * PRIME + ($teamMailingList == null ? 43 : $teamMailingList.hashCode());
      final Object $sprints = this.getSprints();
      result = result * PRIME + ($sprints == null ? 43 : $sprints.hashCode());
      final Object $releases = this.getReleases();
      result = result * PRIME + ($releases == null ? 43 : $releases.hashCode());
      return result;
   }

   public String toString() {
      return "Product(id=" + this.getId() + ", currentIteration=" + this.getCurrentIteration() + ", currentVersion=" + this.getCurrentVersion() + ", code=" + this.getCode() + ", name=" + this.getName() + ", ownerEmail=" + this.getOwnerEmail() + ", ownerName=" + this.getOwnerName() + ", ownerPhone=" + this.getOwnerPhone() + ", teamMailingList=" + this.getTeamMailingList() + ", sprints=" + this.getSprints() + ", releases=" + this.getReleases() + ")";
   }
}

