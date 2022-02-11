package victor.training.ddd.agile.web.dto;

import lombok.Data;

@Data
public class LogHoursRequest {
   public long backlogId;
   public int hours;
}
