package victor.training.ddd.agile.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@Entity
// DDD AggregateRoot
public class Sprint {
   @Id
   @GeneratedValue
   private Long id;
   private int iteration;
   @ManyToOne
   private Product product;
   private LocalDate startDate;
   private LocalDate plannedEndDate;
   private LocalDate endDate;

   @Enumerated(STRING)
   private SprintStatus status = SprintStatus.CREATED;

   @OneToMany(mappedBy = "sprint")
   private List<BacklogItem> items = new ArrayList<>();


   public enum SprintStatus {
      CREATED,
      STARTED,
      FINISHED
   }

   public void end() {
      if (this.status != SprintStatus.STARTED) {
         throw new IllegalStateException();
      }
      this.endDate = LocalDate.now();
      this.status = SprintStatus.FINISHED;
   }

   public void start() {
      if (status != SprintStatus.CREATED) {
         throw new IllegalStateException();
      }
      this.startDate = LocalDate.now();
      this.status = SprintStatus.STARTED;
   }

   public void startItem(long backlogId) {
      if (status != SprintStatus.STARTED) {
         throw new IllegalStateException();
      }
      BacklogItem item = items.stream()
              .filter(it -> it.getId().equals(backlogId))
              .findFirst()
              .orElseThrow();
      item.start();
   }


   public Sprint() {
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

   public SprintStatus getStatus() {
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

}

