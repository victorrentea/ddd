package victor.training.ddd.agile.entity;

import lombok.*;
import victor.training.ddd.agile.dto.CreateSprintRequest;

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
   @Setter(AccessLevel.NONE)
   private int iteration;
//   @ManyToOne
//   private Product product; // illegal in Aggregate design = naive OOP design
   // WHY? when I call spring.startItem(id, id), I DON'T WANT TO BE AFRAID of this agg changing the Product Agg
   // less access to other data = more control of changes = better night sleep :)
   // side benefit (Hibernate): no eager fetching of the data of the product => less columns in the SELECT of a Sprint
   @Setter(AccessLevel.NONE)
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

//   public Sprint(CreateSprintRequest dto) { // you just won a face 2 face code review // Depenency Rule>: Agnostic Domain
      // inside my holy Domain Model
      // date.parse
      // is it enough?
//      iterationNumber = dto.getIteration();
      // - no MapStruct
      // - next episode: sprint.doDto(): Dto <- push all the mapping inside the entity
      // + for Dto as param: less setters in the entity.
      // - what if the DTO is per-client
//   }

   // most details that can be passed at creation can be CHANGED later.

   // if you have so many fields inside (3-4-7), why don't we create a value object to keep those ?

   // option 1:
   //   setter for param1, param2, param...

   // option 2:
//   public void update(param1, param2, param3, param4) {
//
//   }
//   //option 3:
//   public void update(ChangeSprintDto dto) { // ~> Command pattern
////      param1 = dto.param1;
//   }

   public Sprint(int iteration, long productId) {
      this.iteration = iteration;
      this.productId = productId;
   }

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

