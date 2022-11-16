package victor.training.ddd.agile.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import victor.training.ddd.agile.common.DDD.AggregateRoot;
import victor.training.ddd.agile.repo.BacklogItemRepo;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@AggregateRoot // this entity has got the added responsibility to look after all the :child entities inside it that it refers.
@Entity
//@Configurable // dark magic of spring; dont: because this opens the door to inject anything inan aggregate
public class Sprint {
//   @Autowired
//   private BacklogItemRepo backlogItemRepo;


   @Id
   @GeneratedValue
   private Long id;
   private int iteration;
   @ManyToOne
   private Product product;
   private LocalDate startDate; // is NOT null after the sprint was started
   private LocalDate plannedEndDate;
   private LocalDate endDate;


   // fields that only make sense after a certain state = BAD and confusing
   // Instead of having 10 null fields in the first 2-3 statuses, maybe create NEW dedicated Entities if/when you get to the later stage
   // PlacedOrder, ShippedOrder, ReturnedOrder vs a HUGE Order class with 12 null attributes at creation,

   // If it's not clear who's the owner between X <-> Y, then => 2 aggregates.

   public enum Status {
      CREATED,
      STARTED,
      FINISHED
   }

   @Enumerated(STRING)
   private Status status = Status.CREATED;

   @OneToMany(mappedBy = "sprint",
           fetch = FetchType.EAGER, // common if you have an aggregate root-> child entities. when trying to avoid ORM magic (lazy loading)
           cascade = CascadeType.ALL, //
           orphanRemoval = true // common
   )
   private List<BacklogItem> items = new ArrayList<>();
   // this solution works perfect for 10-100 items in the list.
   // BUT what if you have 10K items? can you still do Aggregate Design ?
   // whenever performance hits, DDD back off.
   // a) give up creating an aggregate to span Parent + Child => 2 Agg (Parent Agg vs Child Agg) - each is a standalone agg
   // b) "how can the Sprint handle consistency rules for 10K children"
         // have the parent aggregate(Sprint) make queries to check state of children
         // this class would need a Repo. How the heck can this class @Entity gets an Entity ?
         // b1) NEVER: inject a Repo in the @Entity!
         // b2) pass a repo the methods

   public Sprint() {
   }

   public void startItem(Long itemId/*, BacklogItemRepo repo*/) {
      if (status != Status.STARTED) {
         throw new IllegalStateException();
      }
      // or check that there is no other child with this name
//      repo.findBySprintIdAndNameEqualTo()
//      repo.findById(itemId).orElseThrow().start();
      item(itemId).start();
   }

   public void completeItem(long itemId) {
      if (status != Status.STARTED) {
         throw new IllegalStateException();
      }
      item(itemId).complete();
//      if (items.stream().allMatch(BacklogItem::isDone)) {
//         status = Status.FINISHED;
//      }
   }

   private BacklogItem item(Long itemId) {
      return items.stream().filter(item -> item.getId().equals(itemId)).findFirst().orElseThrow();
   }

   public void end() {
       if (getStatus() != Status.STARTED) {
           throw new IllegalStateException();
       }
       setEndDate(LocalDate.now());
       setStatus(Status.FINISHED);
   }

   public void start() {
      if (this.status != Status.CREATED) {
           throw new IllegalStateException();
       }
      this.startDate = LocalDate.now();
      this.status = Status.STARTED;
   }

   public Long getId() {
      return this.id;
   }

   public int getIteration() {
      return this.iteration;
   }

   public Product getProduct() {
      return this.product;
   }

   public LocalDate getStartDate() {
      return this.startDate;
   }

   public LocalDate getPlannedEndDate() {
      return this.plannedEndDate;
   }

   public LocalDate getEndDate() {
      return this.endDate;
   }

   public Status getStatus() {
      return this.status;
   }

   public List<BacklogItem> getItems() {
      return this.items;
   }

   public Sprint setId(Long id) {
      this.id = id;
      return this;
   }

   public Sprint setIteration(int iteration) {
      this.iteration = iteration;
      return this;
   }

   public Sprint setProduct(Product product) {
      this.product = product;
      return this;
   }

   public Sprint setPlannedEndDate(LocalDate plannedEndDate) {
      this.plannedEndDate = plannedEndDate;
      return this;
   }

   public Sprint setEndDate(LocalDate endDate) {
      this.endDate = endDate;
      return this;
   }

   public Sprint setStatus(Status status) {
      this.status = status;
      return this;
   }

   public Sprint setItems(List<BacklogItem> items) {
      this.items = items;
      return this;
   }


}

