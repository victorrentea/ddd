package victor.training.ddd.agile;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Transactional
@RestController
@RequiredArgsConstructor
class ReleaseController {
   private final ReleaseRepo releaseRepo;
   private final ProductRepo productRepo;
   private final SprintRepo sprintRepo;
   private final BacklogItemRepo backlogItemRepo;

   @PostMapping("product/{productId}/release/{sprintId}")
   public Release createRelease(@PathVariable long productId, @PathVariable long sprintId) {
      Product product = productRepo.findOneById(productId);
      Sprint sprint = sprintRepo.findOneById(sprintId);
      int iteration = sprint.getIteration();

      Release release = product.createRelease(iteration, backlogItemRepo);

      releaseRepo.save(release);
      return release;
   }

}



@Getter
@Setter
@Entity
class Release {
   @Id
   @GeneratedValue
   private Long id;

   private String version;  // eg 1.0, 2.0 ...
   private LocalDate date;
   private Integer sprintIteration;
   private String releaseNotes;

   private Release() {}

   public Release(String version, Integer sprintIteration, String releaseNotes) {
      this.version = version;
      this.sprintIteration = sprintIteration;
      this.releaseNotes = releaseNotes;
      date = LocalDate.now();
   }
}

interface ReleaseRepo extends JpaRepository<Release, Long> {
}