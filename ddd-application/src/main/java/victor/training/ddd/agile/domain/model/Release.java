package victor.training.ddd.agile.domain.model;

import victor.training.ddd.common.DDD.AggregateRoot;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.joining;


@Entity
@AggregateRoot
public class Release {
   @Id
   @GeneratedValue
   private Long id;
//   @ManyToOne
//   private Product product;
   private Long productId; // + FK

   private String version;  // eg 1.0, 2.0 ...
   private LocalDate date;
   @ManyToOne
   private Sprint sprint;

   // TODO delete
   @OneToMany
   @JoinColumn
   private List<ProductBacklogItem> releasedItems; // only used for release notes

private Release() {}
   public Release(String version) {
      this.version = version;
   }

   public String getReleaseNotes() {
      return releasedItems.stream().map(ProductBacklogItem::getTitle).collect(joining("\n"));
   }

   public Long getId() {
      return this.id;
   }

   public Long getProductId() {
      return this.productId;
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

   public List<ProductBacklogItem> getReleasedItems() {
      return this.releasedItems;
   }

   public Release setId(Long id) {
      this.id = id;
      return this;
   }

   public Release setProductId(Long productId) {
      this.productId = productId;
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

   public Release setReleasedItems(List<ProductBacklogItem> releasedItems) {
      this.releasedItems = releasedItems;
      return this;
   }
}

