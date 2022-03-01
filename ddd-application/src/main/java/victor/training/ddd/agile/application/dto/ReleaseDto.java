package victor.training.ddd.agile.application.dto;

import lombok.Data;
import victor.training.ddd.agile.domain.model.Release;

@Data
public class ReleaseDto {
   String releaseNotes;
   String version;

   public ReleaseDto(Release release) {
      releaseNotes = release.getReleaseNotes();
      version = release.getVersion();
   }
}
