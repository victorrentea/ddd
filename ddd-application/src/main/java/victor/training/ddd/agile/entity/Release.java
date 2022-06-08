package victor.training.ddd.agile.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.joining;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Release { // Aggregate with 1 class
   @Id
   @GeneratedValue
   private Long id;
   @ManyToOne
   private Product product; // TODO remove

   private String version;  // eg 1.0, 2.0 ...
   private LocalDate date;
   @ManyToOne
   private Sprint sprint; // TODO remove

   private String releaseNotes;
//   @OneToMany
//   @JoinColumn
//   private List<SprintItem> releasedItems; // only used for release notes illegal from DDD
//
//   public String getReleaseNotes() {
//      return releasedItems.stream().map(SprintItem::getTitle).collect(joining("\n"));
//   }
}
