package victor.training.ddd.agile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.Sprint.Status;
import victor.training.ddd.common.repo.CustomJpaRepository;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.EnumType.STRING;


@Entity
// AggregateRoot is responsible to enforce all contraints spanning bETWEEN the entities inside this Aggregate
public class Sprint {
   @Id
   @GeneratedValue
   private Long id;
   private int iteration;

   // Reference other Aggregates via id (not via object links)
   private Long productId;

   private LocalDate start;
   private LocalDate plannedEnd;
   private LocalDate end;

   public Long getProductId() {
      return productId;
   }

   public Sprint setProductId(Long productId) {
      this.productId = productId;
      return this;
   }

   public Long getId() {
      return id;
   }

   public int getIteration() {
      return iteration;
   }

   public LocalDate getStart() {
      return start;
   }

   public LocalDate getPlannedEnd() {
      return plannedEnd;
   }

   public LocalDate getEnd() {
      return end;
   }

   public Status getStatus() {
      return status;
   }

   public List<BacklogItem> getItems() {
      return items;
   }

   public Sprint setId(Long id) {
      this.id = id;
      return this;
   }

   public Sprint setIteration(int iteration) {
      this.iteration = iteration;
      return this;
   }


   public Sprint setPlannedEnd(LocalDate plannedEnd) {
      this.plannedEnd = plannedEnd;
      return this;
   }

   public void start() {
      if (status != Status.CREATED) {
         throw new IllegalStateException();
      }
      status = Status.STARTED;
      start = LocalDate.now();
   }

   public void finish() {
      if (status != Status.STARTED) {
         throw new IllegalStateException();
      }
      status = Status.FINISHED;
      end = LocalDate.now();
   }

   void startItem(long backlogId) {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      backlogItemById(backlogId).start();
   }

   private BacklogItem backlogItemById(long backlogId) {
      return items.stream().filter(it -> it.getId() == backlogId).findFirst().orElseThrow();
   }

   void completeItem(long backlogId) {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      BacklogItem backlogItem = backlogItemById(backlogId);
      backlogItem.complete();
   }

   void logHours(long backlogId, int hours) {
      BacklogItem backlogItem = backlogItemById(backlogId);
      if (status != Status.STARTED) {
         throw new IllegalStateException("Sprint not started");
      }
      backlogItem.addHours(hours);
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

