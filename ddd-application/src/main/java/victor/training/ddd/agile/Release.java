package victor.training.ddd.agile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@Transactional
@RestController
@RequiredArgsConstructor
class ReleaseController {
   private final ReleaseRepo releaseRepo;
   private final ProductRepo productRepo;
   private final SprintRepo sprintRepo;

   @PostMapping("product/{productId}/release/{sprintId}")
   public Release createRelease(@PathVariable long productId, @PathVariable long sprintId) {
      Product product = productRepo.findOneById(productId);
      Sprint sprint = sprintRepo.findOneById(sprintId);

      int fromIteration = product.getReleases().stream()
          .map(Release::getSprint)
          .mapToInt(Sprint::getIteration)
          .max().orElse(0);
      int toIteration = sprint.getIteration();

      List<BacklogItem> releasedItems = product.getSprints().stream()
          .sorted(Comparator.comparing(Sprint::getIteration))
          .filter(s -> s.getIteration() >= fromIteration && s.getIteration() <= toIteration)
          .flatMap(s -> s.getItems().stream())
          .collect(Collectors.toList());

      Release release = new Release()
          .setProduct(product)
          .setSprint(sprint)
          .setReleasedItems(releasedItems)
          .setDate(LocalDate.now())
          .setVersion(product.incrementAndGetVersion() + ".0");
      product.getReleases().add(release);

      releaseRepo.save(release);
      return release;
   }
}



@Getter
@Setter
@NoArgsConstructor
@Entity
class Release {
   @Id
   @GeneratedValue
   private Long id;
   @ManyToOne
   private Product product;

   private String version;  // eg 1.0, 2.0 ...
   private LocalDate date;
   @ManyToOne
   private Sprint sprint;

   @OneToMany
   @JoinColumn
   private List<BacklogItem> releasedItems; // only used for release notes

   public String getReleaseNotes() {
      return releasedItems.stream().map(BacklogItem::getTitle).collect(joining("\n"));
   }
}

interface ReleaseRepo extends JpaRepository<Release, Long> {
}