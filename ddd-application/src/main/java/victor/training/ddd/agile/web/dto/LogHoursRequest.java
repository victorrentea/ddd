package victor.training.ddd.agile.web.dto;

import lombok.Data;

@Data
public class LogHoursRequest {
   public long sprintBacklogItemId;
   public int hours;
}
