package victor.training.ddd.agile.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;


@Data
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

   public int incrementAndGetIteration() {
      currentIteration++;
      return currentIteration;
   }

   public int incrementAndGetVersion() {
      currentVersion++;
      return currentVersion;
   }
}

