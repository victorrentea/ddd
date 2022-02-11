package victor.training.ddd.agile.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;


@Entity
public class Release { // a private child of the Product Aggregate.
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