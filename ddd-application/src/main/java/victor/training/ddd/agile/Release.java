package victor.training.ddd.agile;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Comparator;

import static java.util.stream.Collectors.joining;

@Transactional
@RestController
@RequiredArgsConstructor
class ReleaseController {
//   private final ReleaseRepo releaseRepo;
   private final ProductRepo productRepo;
   private final SprintRepo sprintRepo;

   @PostMapping("product/{productId}/release/{sprintId}")
   public void createRelease(@PathVariable long productId, @PathVariable long sprintId) {
      Product product = productRepo.findOneById(productId);
      Sprint sprint = sprintRepo.findOneById(sprintId);


      String releasedNotes = computeReleaseNotes(sprint);

      product.addRelease(sprint, releasedNotes);
   }


//   @Async
   @EventListener
//   @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
   public void onBacklogItemTitleChanged(BacklogItemTitleChangedEvent event) {
      Long backlogItemId = event.getBacklogItemId();
      // determine relase to update
      Release release;
      release.setReleaseNotes(computeReleaseNotes(backlogItem...sprint))
   }
   
   private String computeReleaseNotes(Sprint sprint) {
      Product product = sprint.getProduct();
      int fromIteration = product.getReleases().stream()
          .map(Release::getSprintId)
          .map(sprintRepo::findOneById)
          .mapToInt(Sprint::getIteration)
          .max().orElse(0);
      int toIteration = sprint.getIteration();

      String releasedNotes = product.getSprints().stream()
          .sorted(Comparator.comparing(Sprint::getIteration))
          .filter(s -> s.getIteration() >= fromIteration && s.getIteration() <= toIteration)
          .flatMap(s -> s.getItems().stream())
          .map(BacklogItem::getTitle)
          .collect(joining("\n"));
      return releasedNotes;
   }

//   @EventLi
//
//   public void onNotesChanged() {
//
//   }
}



@Entity
class Release { // a private child of the Product Aggregate.
   @Id
   @GeneratedValue
   private Long id;
   private String comments;
   private String version;  // eg 1.0, 2.0 ...
   private LocalDate date = LocalDate.now();
   private Long sprintId;
   private String releaseNotes;

   private Release() {}
   public Release(Long sprintId, String releaseNotes, String version) {
      this.sprintId = sprintId;
      this.releaseNotes = releaseNotes;
      this.version = version;
   }

   public Release setReleaseNotes(String releaseNotes) {
      this.releaseNotes = releaseNotes;
      return this;
   }

   // changes that can't violate any rule are allowed to child entities
   public Release setComments(String comments) {
      this.comments = comments;
      return this;
   }
   public Long getSprintId() {
      return sprintId;
   }
   public String getReleaseNotes() {
      return releaseNotes;
   }
   public Long getId() {
      return this.id;
   }

   public LocalDate getDate() {
      return this.date;
   }

   public Release setId(Long id) {
      this.id = id;
      return this;
   }

}

//interface ReleaseRepo extends JpaRepository<Release, Long> {
//}