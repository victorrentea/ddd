package victor.training.ddd.agile.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddBacklogItemRequest {
//   public String desiredId; // why would you ever allow the client to set the id of the enitity you create ?
      // 1) it has MEANING for the client : eg the url of a blog post. (human readable)
      // 2) it is external from a 3rd party
      // 3) client generate an UUID and in case of timeout are ALLOWED to retry the same ID.
            // > IDEMPOTENT HTTP ENDPOINT when you receive such a request twice, they will share the desiredId >PK violation i you try to persist again
   public long backlogId;
   public int fpEstimation;
}