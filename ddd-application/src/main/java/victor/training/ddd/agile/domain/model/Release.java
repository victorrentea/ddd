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

   @OneToMany
   @JoinColumn
   private List<BacklogItem> releasedItems; // only used for release notes

   public Release() { // for hibernate
   }

   public String getReleaseNotes() {
      return releasedItems.stream().map(BacklogItem::getTitle).collect(joining("\n"));
   }

   public Long getId() {
      return this.id;
   }

   public Product getProduct() {
      return this.product;
   }

   public String getVersion() {
      return this.version;
   }

   public LocalDate getDate() {
      return this.date;
   }

   public Sprint getSprint() {
      return this.sprint;
   }

   public List<BacklogItem> getReleasedItems() {
      return this.releasedItems;
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

   public Release setReleasedItems(List<BacklogItem> releasedItems) {
      this.releasedItems = releasedItems;
      return this;
   }
}
