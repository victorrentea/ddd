package victor.training.ddd.agile;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.events2.DomainEventsPublisher;
import victor.training.ddd.common.repo.CustomJpaRepository;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Getter
@NoArgsConstructor
@Entity
public class BacklogItem /*extends AbstractAggregateRoot*/ {
   @Id
   @GeneratedValue
   private Long id;
   @ManyToOne
   private Product product;

   //   private Long productId;
   private String title;
   private String description;


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
   @Version
   private Long version;

   private int hoursConsumed;

   public void start() {
      if (this.status != Status.CREATED) {
         throw new IllegalStateException("Item already started");
      }
      this.status = Status.STARTED;
   }

   public void complete(IReleaseServicePortForBacklogItem releaseService) {
      if (this.status != Status.STARTED) {
         throw new IllegalStateException("Cannot complete an Item before starting it");
      }
      this.status = Status.DONE;
//      releaseService.updatereleaseNotes(id)
      DomainEventsPublisher.publish(new MyEvent());
//      publisher.publishEvent();
   }


   public void addHours(int hours) {
      hoursConsumed += hours;
   }

   public BacklogItem setId(Long id) {
      this.id = id;
      return this;
   }

   public BacklogItem setProduct(Product product) {
      this.product = product;
      return this;
   }

   public BacklogItem setTitle(String title) {
      this.title = title;
      return this;
   }

   public BacklogItem setDescription(String description) {
      this.description = description;
      return this;
   }

   void setSprint(Sprint sprint) {
      this.sprint = sprint;
   }

   void setFpEstimation(Integer fpEstimation) {
      this.fpEstimation = fpEstimation;
   }

   public BacklogItem setVersion(Long version) {
      this.version = version;
      return this;
   }

   public BacklogItem setHoursConsumed(int hoursConsumed) {
      this.hoursConsumed = hoursConsumed;
      return this;
   }
}

