package victor.training.ddd.agile.domain.model;

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
public class Release {
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

