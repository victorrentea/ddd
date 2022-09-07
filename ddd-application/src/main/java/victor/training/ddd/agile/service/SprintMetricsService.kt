package victor.training.ddd.agile.service

import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import victor.training.ddd.agile.dto.SprintMetrics
import victor.training.ddd.agile.entity.BacklogItem
import victor.training.ddd.agile.entity.Sprint
import victor.training.ddd.agile.entity.SprintItem
import victor.training.ddd.agile.repo.SprintRepo
import java.util.stream.Collectors

@Service
class SprintMetricsService(private val sprintRepo: SprintRepo) {
    fun getSprintMetrics(@PathVariable id: Long): SprintMetrics {
        val sprint = sprintRepo.findOneById(id)
        check(sprint.status() == Sprint.SprintStatus.FINISHED)
        val doneItems = sprint.items().stream()
            .filter { item-> item.status == SprintItem.ItemStatus.DONE }
            .collect(Collectors.toList())
        val dto = SprintMetrics()
        dto.consumedHours = sprint.items().stream().mapToInt { obj-> obj.hoursConsumed() }.sum()
        dto.calendarDays = sprint.startDate()?.until(sprint.endDate())?.days?:0
        dto.doneFP = doneItems.stream().mapToInt { obj-> obj.fpEstimation }.sum()
        dto.fpVelocity = 1.0 * dto.doneFP / dto.consumedHours
        dto.hoursConsumedForNotDone = sprint.items().stream()
            .filter { item-> item.status != SprintItem.ItemStatus.DONE }
            .mapToInt { obj-> obj.hoursConsumed() }.sum()
        if (sprint.endDate()!!.isAfter(sprint.plannedEndDate)) {
            dto.delayDays = sprint.plannedEndDate!!.until(sprint.endDate()).days
        }
        return dto
    }
}