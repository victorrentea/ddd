package victor.training.ddd.agile.dto;

import lombok.Data;

@Data
public class SprintMetrics {
   private int consumedHours;
   private int doneFP;
   private double fpVelocity;
   private int hoursConsumedForNotDone;
   private int calendarDays;
   private int delayDays;

}