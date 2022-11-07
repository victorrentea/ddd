package victor.training.ddd.agile.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@NoArgsConstructor
@Entity
@Data
// AggregateRoot { BacklogItem }
public class Sprint {
   @Id
   @GeneratedValue
   private Long id;
   private int iteration;
//   @ManyToOne
//   private Product product; // illegal in Aggregate design = naive OOP design
   // WHY? when I call spring.startItem(id, id), I DON'T WANT TO BE AFRAID of this agg changing the Product Agg
   // less access to other data = more control of changes = better night sleep :)
   // side benefit (Hibernate): no eager fetching of the data of the product => less columns in the SELECT of a Sprint
   private Long productId; // ! keep the FK, do not sacrifice consistency prematurely

   private LocalDate startDate;
   private LocalDate plannedEndDate;
   private LocalDate endDate;


   public enum Status {
      CREATED,
      STARTED,
      FINISHED
   }

//   @Version
//   private LocalDateTime lastChangeTime;

   @Enumerated(STRING)
   private Status status = Status.CREATED;

   @OneToMany(mappedBy = "sprint",
           cascade = CascadeType.ALL,
           fetch = FetchType.EAGER)
   private List<BacklogItem> items = new ArrayList<>();

   public void startItem(long backlogId) {
      if (status != Status.STARTED) {
         throw new IllegalStateException();
      }
      findItemById(backlogId).start();
   }

   private BacklogItem findItemById(long backlogId) {
      return items.stream()
              .filter(i -> i.getId().equals(backlogId)).findFirst().orElseThrow();
   }



}

