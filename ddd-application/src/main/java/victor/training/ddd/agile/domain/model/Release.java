package victor.training.ddd.agile.domain.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.joining;


@Entity
public class Release {
   @Id
   @GeneratedValue
   private Long id;
   @ManyToOne
   private Product product;

   private String version;  // eg 1.0, 2.0 ...
   private LocalDate date;
   @ManyToOne
   private Sprint sprint;

   private String releaseNotes;

   public Release() { // for hibernate
   }

   public Release setReleaseNotes(String releaseNotes) {
      this.releaseNotes = releaseNotes;
      return this;
   }

   public String getReleaseNotes() {
      return releaseNotes;
   }

   public Long getId() {
      return id;
   }

   public Product getProduct() {
      return product;
   }

   public String getVersion() {
      return version;
   }

   public LocalDate getDate() {
      return date;
   }

   public Sprint getSprint() {
      return sprint;
   }


   public Release setId(Long id) {
      this.id = id;
      return this;
   }

   public Release setProduct(Product product) {
      this.product = product;
      return this;
   }

   public Release setVersion(String version) {
      this.version = version;
      return this;
   }

   public Release setDate(LocalDate date) {
      this.date = date;
      return this;
   }

   public Release setSprint(Sprint sprint) {
      this.sprint = sprint;
      return this;
   }

}
