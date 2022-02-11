package victor.training.ddd.agile.web.dto;

import lombok.Data;

@Data
public class AddBacklogItemRequest {
   public long productBacklogItemId;
   public int fpEstimation;
}



