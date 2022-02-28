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

