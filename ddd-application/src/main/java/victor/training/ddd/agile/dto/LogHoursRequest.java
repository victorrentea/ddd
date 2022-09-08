package victor.training.ddd.agile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogHoursRequest {
   private long backlogId;
   private int hours;
}