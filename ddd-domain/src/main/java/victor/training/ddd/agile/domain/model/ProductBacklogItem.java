package victor.training.ddd.agile.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;


@Getter
@NoArgsConstructor
@Entity
public class ProductBacklogItem extends AbstractAggregateRoot<ProductBacklogItem> {
   @Id
   @GeneratedValue
   private Long id;
   @ManyToOne
   private Product product;
   //   private Long productId;
   private String title;
   private String description;

   private LocalDateTime createTime = LocalDateTime.now(); // field readonly after creation
   @Version
   private Long version;


   // ------- from here below, only make sense after the BI is added to Spring

   // Could these two --> @Embeddable
   @ManyToOne
   private Sprint sprint; // ⚠ not NULL when assigned to a sprint
   private Integer fpEstimation; // ⚠ not NULL when assigned to a sprint

   public enum Status {
      CREATED,
      STARTED,
      DONE
   }

   @Enumerated(STRING)
   private Status status = Status.CREATED;

   private int hoursConsumed;

   public void start() {
      if (status != Status.CREATED) {
         throw new IllegalStateException("Item already started");
      }
      status = Status.STARTED;
   }

   public void complete() {
      if (status != Status.STARTED) {
         throw new IllegalStateException("Cannot complete an Item before starting it");
      }
      status = Status.DONE;
   }


   public void addHours(int hours) {
      hoursConsumed += hours;
   }

   public ProductBacklogItem setId(Long id) {
      this.id = id;
      return this;
   }

   public ProductBacklogItem setProduct(Product product) {
      this.product = product;
      return this;
   }

   public ProductBacklogItem setTitle(String title) {
      this.title = title;
      return this;
   }

   public ProductBacklogItem setDescription(String description) {
      this.description = description;
      return this;
   }

   void setSprint(Sprint sprint) {
      this.sprint = sprint;
   }

   void setFpEstimation(Integer fpEstimation) {
      this.fpEstimation = fpEstimation;
   }

   public ProductBacklogItem setVersion(Long version) {
      this.version = version;
      return this;
   }

   public ProductBacklogItem setHoursConsumed(int hoursConsumed) { // TODO Victor 2022-02-11:
      this.hoursConsumed = hoursConsumed;
      return this;
   }
}

