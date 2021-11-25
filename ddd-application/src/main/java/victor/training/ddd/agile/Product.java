package victor.training.ddd.agile;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.Sprint.Status;
import victor.training.ddd.common.repo.CustomJpaRepository;

import javax.persistence.*;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;


@Data
@Entity
public class Product {
   @Id
   @GeneratedValue
   private Long id;
   private int currentIteration = 0;
   private int currentVersion = 0;
   @NotNull
   @Length(min = 3, max = 3)
   private String code;
   @NotNull
   private String name;
   @NotNull
   private String mailingList;

   // TODO extract @Embeddable
   private String ownerEmail;
   private String ownerName;
   private String ownerPhone;
   private String teamMailingList;


//   @Version
//   private Long version;
//
//   public void markAsDirty() {
//      version ++;
//   }


   @OneToMany
   @JoinColumn
   private List<Release> releases = new ArrayList<>();

   protected Product() { // doar pt hibernate
   }
   public Product(String code, String name, String mailingList) {
//      if (Objects.requireNonNull(code,"code is required").length() != 3) {
//         throw new IllegalArgumentException("code size should be 3");
//      }
      this.code = code;
      this.name = name;
      this.mailingList = mailingList;

      getValidator().validate(this);
   }

   private Validator getValidator() {
      return Validation.buildDefaultValidatorFactory().getValidator();
   }

   private int nextIterationNumber() {
      return ++ currentIteration;
   }

   private String nextReleaseVersion() {
      return ( ++ currentVersion) + ".0";
   }

   public Release createRelease(Sprint sprint, BacklogItemRepo backlogItemRepo) {
      if (sprint.getStatus() != Status.FINISHED) {
         throw new IllegalArgumentException();
      }
      int lastReleasedIteration = releases.stream()
          .mapToInt(Release::getSprintIteration)
          .max()
          .orElse(0);

      List<BacklogItem> releasedItems = backlogItemRepo.findDoneItemsBetweenIterations(lastReleasedIteration + 1, sprint.getIteration());

      String releaseNotes = releasedItems.stream().map(BacklogItem::getTitle).collect(joining("\n"));

      Release release = new Release(nextReleaseVersion() , sprint.getIteration(), releaseNotes );
      releases.add(release);
      return release;
   }

   public Sprint createSprint(LocalDate plannedEnd) {
      int iteration = nextIterationNumber();
      SprintId sprintId = new SprintId(getCode(), iteration);
      return new Sprint(sprintId, plannedEnd);
   }
}

