package victor.training.ddd.agile.domain.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
   private LocalDate start;
   private LocalDate plannedEnd;
   private LocalDate end;

   public Sprint() {
   }

   public void checkSprintMatchesAndStarted(BacklogItem backlogItem) {
      if (!backlogItem.getSprint().getId().equals(getId())) {
         throw new IllegalArgumentException("item not in sprint");
      }
      if (getStatus() != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
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

   public LocalDate getStart() {
      return this.start;
   }

   public LocalDate getPlannedEnd() {
      return this.plannedEnd;
   }

   public LocalDate getEnd() {
      return this.end;
   }

   public Status getStatus() {
      return this.status;
   }

   public List<BacklogItem> getItems() {
      return Collections.unmodifiableList(this.items);
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

   public Sprint setStart(LocalDate start) {
      this.start = start;
      return this;
   }

   public Sprint setPlannedEnd(LocalDate plannedEnd) {
      this.plannedEnd = plannedEnd;
      return this;
   }

   public Sprint setEnd(LocalDate end) {
      this.end = end;
      return this;
   }

   public Sprint setStatus(Status status) {
      this.status = status;
      return this;
   }

   public void addItem(BacklogItem backlogItem, int fpEstimation) {
      if (status != Status.CREATED) {
         throw new IllegalStateException("Can only add items to Sprint before it starts");
      }
      backlogItem.setFpEstimation(fpEstimation);
      items.add(backlogItem);
      backlogItem.setSprint(this); // you will see the setter from here
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

