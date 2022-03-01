package victor.training.ddd.agile.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
   @NoArgsConstructor
   @AllArgsConstructor
   public class LogHoursRequest {
      public String backlogId;
      public int hours;
   }