package victor.training.ddd.agile.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@Entity
public class Sprint {
   @Id
   @GeneratedValue
   private Long id;
   private int iteration;
   @ManyToOne
   private Product product;
   private LocalDate startDate; // is NOT null after the sprint was started
   private LocalDate plannedEndDate;
   private LocalDate endDate;

   public Sprint() {
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

   public enum Status {
      CREATED,
      STARTED,
      FINISHED
   }

   @Enumerated(STRING)
   private Status status = Status.CREATED;

   @OneToMany(mappedBy = "sprint")
   private List<BacklogItem> items = new ArrayList<>();
}

