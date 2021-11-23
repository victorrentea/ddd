package victor.training.ddd.agile;

import org.springframework.stereotype.Service;
import victor.training.ddd.agile.SprintController.SprintMetrics;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class SprintMetricsCalculator {
   public SprintMetrics computeMetrics(Sprint sprint) {
      SprintMetrics dto = new SprintMetrics();
      List<SprintBacklogItem> doneItems = sprint.getItems().stream()
          .filter(item -> item.getStatus() == SprintBacklogItem.Status.DONE)
          .collect(toList());
      dto.consumedHours = sprint.getItems().stream().mapToInt(SprintBacklogItem::getHoursConsumed).sum();
      dto.calendarDays = sprint.getStart().until(sprint.getEnd()).getDays();
      dto.doneFP = doneItems.stream().mapToInt(SprintBacklogItem::getFpEstimation).sum();
      dto.fpVelocity = 1.0 * dto.doneFP / dto.consumedHours;
      dto.hoursConsumedForNotDone = sprint.getItems().stream()
          .filter(item -> item.getStatus() != SprintBacklogItem.Status.DONE)
          .mapToInt(SprintBacklogItem::getHoursConsumed).sum();
      if (sprint.getEnd().isAfter(sprint.getPlannedEnd())) {
         dto.delayDays = sprint.getPlannedEnd().until(sprint.getEnd()).getDays();
      }
      return dto;
   }
}
