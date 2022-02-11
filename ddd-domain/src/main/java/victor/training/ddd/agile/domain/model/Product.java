package victor.training.ddd.agile.domain.model;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;


// AGGREGATE = a set of objects ENTIT + VO
// AGGREGATE ROOT = one of those ENTITIES ("the parent") whose
// repons is to enforce the consistency of the entier AGGREGATE by encapsulating
// all changes to anything insidee the AGGREGATE THAT can lead to inconsistencies
@Getter
@Entity //= aggregate root
public class Product {
   @Id
   @GeneratedValue
   private Long id;
   private int currentIteration = 0;
   private int currentVersion = 0;
   private String code;
   @NotNull
//   @Column(nullable = false) or NOT NULL in incrementals (flyway)
   private String name;

   @Embedded
   private Contact owner;
   private String teamMailingList;

   @OneToMany(mappedBy = "product")
   private List<BacklogItem> backlogItems = new ArrayList<>();
   @OneToMany(mappedBy = "product")
   private List<Sprint> sprints = new ArrayList<>();

   //private children
   @JoinColumn
   @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
   private List<Release> releases = new ArrayList<>();

   private Product() {} // just for hinernate, not needed in playout
   public Product(String code, String name, String teamMailingList) {
      setCode(code);
      this.name = Objects.requireNonNull(name);
      this.teamMailingList = Objects.requireNonNull(teamMailingList);
   }

   public Product setCode(String code) {
      this.code = Objects.requireNonNull(code);
      return this;
   }

   public int incrementAndGetIteration() {
      return ++ currentIteration;
   }

   private int incrementAndGetVersion() {
      return ++ currentVersion;
   }

   public Long addRelease(Sprint sprint) {
      String releaseNotes = computeReleaseNotes(sprint.getIterationNumber());
      Release release = new Release(sprint.getId(),
          releaseNotes,
          incrementAndGetVersion() + ".0",
          sprint.getIterationNumber());
      getReleases().add(release);
      return release.getId();
   }

   private int getLastReleasedIterationNumber() {
      return getReleases().stream()
          .mapToInt(Release::getIterationNumber)
          .max()
          .orElse(0);
   }

   private String computeReleaseNotes(int toIterationNumber) {
      int fromIterationNumber = getLastReleasedIterationNumber();

      List<Sprint> releasedIterations = getSprints().stream()
          .sorted(Comparator.comparing(Sprint::getIterationNumber))
          .filter(s -> s.getIterationNumber() >= fromIterationNumber && s.getIterationNumber() <= toIterationNumber)
          .collect(toList());

      return releasedIterations.stream()
          .flatMap(s -> s.getItems().stream())
          .map(BacklogItem::getTitle)
          .collect(joining("\n"));
   }
}

