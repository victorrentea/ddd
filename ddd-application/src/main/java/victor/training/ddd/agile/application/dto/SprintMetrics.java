package victor.training.ddd.agile.application.dto;

import lombok.Data;

@Data
public class SprintMetrics {
   public int consumedHours;
   public int doneFP;
   public double fpVelocity;
   public int hoursConsumedForNotDone;
   public int calendarDays;
   public int delayDays;
}