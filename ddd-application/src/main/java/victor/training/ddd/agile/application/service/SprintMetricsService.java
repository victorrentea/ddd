package victor.training.ddd.agile.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.ddd.agile.application.dto.SprintMetrics;
import victor.training.ddd.agile.domain.model.Sprint;
import victor.training.ddd.agile.domain.model.Sprint.Status;
import victor.training.ddd.agile.domain.model.SprintBacklogItem;
import victor.training.ddd.agile.domain.repo.SprintRepo;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SprintMetricsService {
   private final SprintRepo sprintRepo;

   public SprintMetrics computeMetrics(long id) {
      Sprint sprint = sprintRepo.findOneById(id);
      if (sprint.getStatus() != Status.FINISHED) {
         throw new IllegalStateException();
      }
      List<SprintBacklogItem> doneItems = sprint.getItems().stream()
          .filter(item -> item.getStatus() == SprintBacklogItem.Status.DONE)
          .collect(Collectors.toList());
      SprintMetrics dto = new SprintMetrics();
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
