package victor.training.ddd.agile.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class BacklogItem {
   @Id
   @GeneratedValue
   private Long id;
   @ManyToOne
   private Product product;
   private String title;
   private String description;

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
      hoursConsumed += hours;
   }

}
