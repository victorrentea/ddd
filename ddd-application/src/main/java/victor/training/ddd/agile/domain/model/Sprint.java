package victor.training.ddd.agile.domain.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static javax.persistence.EnumType.STRING;

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

   public Sprint() {
   }

   public Long getId() {
      return this.id;
   }

   public int getIteration() {
      return this.iteration;
   }

   public Long getProductId() {
      return this.productId;
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

   public Sprint setId(Long id) {
      this.id = id;
      return this;
   }

   public Sprint setStartDate(LocalDate startDate) {
      this.startDate = startDate;
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

//   @Version
//   private LocalDateTime lastChangeTime;

   @Enumerated(STRING)
   private Status status = Status.CREATED;


   // JPA2.1 introduced OneToMany UNIDIRECTIONAL
   @OneToMany(mappedBy = "sprint",
           cascade = CascadeType.ALL,
           fetch = FetchType.EAGER)
   @JoinColumn // otherwise a new weird table appears
   private List<BacklogItem> items = new ArrayList<>();

   public List<BacklogItem> getItems() {
      return Collections.unmodifiableList(items);
   }

   public void addItem(BacklogItem item) {
      items.add(item);
   }

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
      checkSprintStarted();
      findItemById(backlogId).start();
   }

   private void checkSprintStarted() {
      if (status != Status.STARTED) {
         throw new IllegalStateException();
      }
   }


   public void completeItem(long backlogId) {
      checkSprintStarted();
      findItemById(backlogId).complete();
   }

   public void logHoursForItem(long backlogId, int hours) {
      checkSprintStarted();
      findItemById(backlogId).addHours(hours);
   }

   private BacklogItem findItemById(long backlogId) {
      return items.stream()
              .filter(i -> i.getId().equals(backlogId)).findFirst().orElseThrow();
   }



}

