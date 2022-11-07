package victor.training.ddd.agile.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@ToString
@Setter// consider encapsulating changes
@Getter
@Entity
public class Product {
   @Id
   @GeneratedValue
   private Long id;
   private int currentIteration = 0;
   private int currentVersion = 0;
   @Setter(AccessLevel.NONE)
   private String code;
   private String name;

   @Embedded
   private ProductOwner productOwner;

   private String teamMailingList;

   @ToString.Exclude
   @OneToMany(mappedBy = "product")
   private List<Sprint> sprints = new ArrayList<>();

   @ToString.Exclude
   @OneToMany(mappedBy = "product")
   private List<Release> releases = new ArrayList<>();

   protected Product() { // for the eyes of Hibernate only
   }

   public Product(String code, String name) {
      this.code = requireNonNull(code);
      setName(name);
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
}

