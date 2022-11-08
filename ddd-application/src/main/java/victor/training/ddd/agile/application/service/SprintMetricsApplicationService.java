package victor.training.ddd.agile.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.ddd.agile.application.dto.SprintMetrics;
import victor.training.ddd.agile.domain.model.BacklogItem;
import victor.training.ddd.agile.domain.model.Sprint;
import victor.training.ddd.agile.domain.model.Sprint.Status;
import victor.training.ddd.agile.domain.model.SprintItem;
import victor.training.ddd.agile.domain.repo.SprintRepo;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class SprintMetricsApplicationService {
    private final SprintRepo sprintRepo;

    @GetMapping("sprint/{sprintId}/metrics")
    @Transactional
    public SprintMetrics getSprintMetrics(@PathVariable long sprintId) {
        Sprint sprint = sprintRepo.findOneById(sprintId);
        if (sprint.getStatus() != Status.FINISHED) {
            throw new IllegalStateException();
        }
        List<SprintItem> doneItems = sprint.getItems().stream()
                .filter(item -> item.getStatus() == SprintItem.Status.DONE)
                .collect(Collectors.toList());
        SprintMetrics dto = new SprintMetrics();
        dto.setConsumedHours(sprint.getItems().stream().mapToInt(SprintItem::getHoursConsumed).sum());
        dto.setCalendarDays(sprint.getStartDate().until(sprint.getEndDate()).getDays());
        dto.setDoneFP(doneItems.stream().mapToInt(SprintItem::getFpEstimation).sum());
        dto.setFpVelocity(1.0 * dto.getDoneFP() / dto.getConsumedHours());
        dto.setHoursConsumedForNotDone(sprint.getItems().stream()
                .filter(item -> item.getStatus() != SprintItem.Status.DONE)
                .mapToInt(SprintItem::getHoursConsumed).sum());
        if (sprint.getEndDate().isAfter(sprint.getPlannedEndDate())) {
            dto.setDelayDays(sprint.getPlannedEndDate().until(sprint.getEndDate()).getDays());
        }
        return dto;
    }

}
