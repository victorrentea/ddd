package victor.training.ddd.agile;

import lombok.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.common.repo.CustomJpaRepository;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@NoArgsConstructor
@Entity
// Child Entity of the Sprint Aggregate
public class BacklogItem {
   @Id
   @GeneratedValue
   private Long id;
   @ManyToOne
   private Product product;
   private String title;
   private String description;

   public void start() {
      if (status != Status.CREATED) {
         throw new IllegalStateException("Item already started");
      }
      status = Status.STARTED;
   }
   public void complete() {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Item already started");
      }
      status = Status.DONE;
   }

   public enum Status {
      CREATED,
      STARTED,
      DONE
   }
   @Enumerated(STRING)
   private Status status = Status.CREATED;

   @ManyToOne
   private Sprint sprint; // ⚠ not NULL when assigned to a sprint
   private Integer fpEstimation; // ⚠ not NULL when assigned to a sprint
   private Integer hoursConsumed; // ⚠ not NULL when assigned to a sprint

   @Version
   private Long version;

   public void addHours(int hours) {
      if (this.status != BacklogItem.Status.STARTED) {
         throw new IllegalStateException("Item not started");
      }
      hoursConsumed += hours;
   }

}

