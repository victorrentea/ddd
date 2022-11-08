package victor.training.ddd.agile.domain.model;

import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

// consider encapsulating changes
@Entity
public class Product {
   @Id
   @GeneratedValue
   private Long id;
   private int currentIteration = 0;
   private int currentVersion = 0;

   @Column(nullable = false) // NOT NULL
   private String code;
   @Column(nullable = false) // NOT NULL
   private String name;

   @Embedded
   private ProductOwner productOwner;

   private String teamMailingList;

//   @ToString.Exclude
//   @OneToMany(mappedBy = "product")
//   private List<Sprint> sprints = new ArrayList<>();

   @OneToMany(mappedBy = "product")
   private List<Release> releases = new ArrayList<>();

   protected Product() { // for the eyes of Hibernate only
   }

   public Product(String code, String name) {
      this.code = requireNonNull(code);
      setName(name);
   }

    public void updatePOPhone(String newPhone) {
        ProductOwner po = getProductOwner();
        ProductOwner newPo = po.withOwnerPhone(newPhone);
        setProductOwner(newPo);
    }


    public Product setName(String name) {
      if (StringUtils.isBlank(name)) {
         throw new IllegalArgumentException("Illegal name");
      }
      this.name = name;
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

    public ProductOwner getProductOwner() {
        return productOwner;
    }

    public String getTeamMailingList() {
        return teamMailingList;
    }

    public List<Release> getReleases() {
        return releases;
    }

    public Product setId(Long id) {
        this.id = id;
        return this;
    }

    public Product setProductOwner(ProductOwner productOwner) {
        this.productOwner = productOwner;
        return this;
    }

    public Product setTeamMailingList(String teamMailingList) {
        this.teamMailingList = teamMailingList;
        return this;
    }

    public String toString() {
        return "Product(id=" + getId() + ", currentIteration=" + getCurrentIteration() + ", currentVersion=" + getCurrentVersion() + ", code=" + getCode() + ", name=" + getName() + ", productOwner=" + getProductOwner() + ", teamMailingList=" + getTeamMailingList() + ")";
    }
}

