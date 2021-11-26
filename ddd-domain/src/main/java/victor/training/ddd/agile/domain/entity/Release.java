package victor.training.ddd.agile.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;


@Getter
@Setter
@Entity
public class Release {
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

