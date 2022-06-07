package victor.training.ddd.agile.service;

import org.springframework.stereotype.Component;
import victor.training.ddd.agile.dto.SprintMetrics;
import victor.training.ddd.agile.entity.BacklogItem;
import victor.training.ddd.agile.entity.Sprint;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class SprintMetricsGenerator {

    public SprintMetrics computeMetrics(Sprint sprint) {
        if (!sprint.isFinished()) {
            throw new IllegalStateException();
        }
        List<BacklogItem> doneItems = sprint.getItems().stream()
                .filter(item -> item.getStatus() == BacklogItem.Status.COMPLETED)
                .collect(Collectors.toList());
        SprintMetrics metrics = new SprintMetrics();
        metrics.consumedHours = sprint.getItems().stream().mapToInt(BacklogItem::getHoursConsumed).sum();
        metrics.calendarDays = sprint.getStartDate().until(sprint.getEndDate()).getDays();
        metrics.doneFP = doneItems.stream().mapToInt(BacklogItem::getFpEstimation).sum();
        metrics.fpVelocity = 1.0 * metrics.doneFP / metrics.consumedHours;
        metrics.hoursConsumedForNotDone = sprint.getItems().stream()
                .filter(item -> item.getStatus() != BacklogItem.Status.COMPLETED)
                .mapToInt(BacklogItem::getHoursConsumed).sum();
        if (sprint.getEndDate().isAfter(sprint.getPlannedEndDate())) {
            metrics.delayDays = sprint.getPlannedEndDate().until(sprint.getEndDate()).getDays();
        }
        return metrics;
    }

}