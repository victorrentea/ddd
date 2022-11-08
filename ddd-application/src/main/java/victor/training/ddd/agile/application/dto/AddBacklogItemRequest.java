package victor.training.ddd.agile.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddBacklogItemRequest {
   private long backlogId;
   private int fpEstimation;
}