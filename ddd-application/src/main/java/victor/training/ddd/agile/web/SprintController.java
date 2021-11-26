package victor.training.ddd.agile.web;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.application.SprintApplicationService;
import victor.training.ddd.agile.domain.entity.Sprint;
import victor.training.ddd.agile.domain.entity.SprintBacklogItem;
import victor.training.ddd.agile.domain.entity.SprintId;
import victor.training.ddd.agile.domain.entity.SprintMetrics;
import victor.training.ddd.agile.domain.repo.SprintRepo;
import victor.training.ddd.agile.domain.service.SprintMetricsCalculator;

import java.time.LocalDate;

@Transactional
@RestController
@RequiredArgsConstructor
public class SprintController {
   private final SprintRepo sprintRepo;
   private final SprintMetricsCalculator sprintMetricsCalculator;
   private final SprintApplicationService applicationService;


   @Transactional
   @PostMapping("sprint")
   public SprintId createSprint(@RequestBody SprintDto dto) {
      return applicationService.createSprint(dto);
   }

   @GetMapping("sprint/{id}")
   public Sprint getSprint(@PathVariable SprintId id) { // TODO return SprintDto nu entity in afara ca ii cuplezi pe dushmani (FE/clientI) la modelul tau intern
      return applicationService.getSprint(id);
   }

   @PostMapping("sprint/{id}/start")
   public void startSprint(@PathVariable SprintId id) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.start();
   }

   @PostMapping("sprint/{id}/finish")
   public void finishSprint(@PathVariable SprintId id) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.finishSprint(id);
   }

   @GetMapping("sprint/{id}/metrics")
   public SprintMetrics getSprintMetrics(@PathVariable SprintId id) {
      Sprint sprint = sprintRepo.findOneById(id);
      if (sprint.isFinished()) {
         throw new IllegalStateException();
      }
      return sprintMetricsCalculator.computeMetrics(sprint);
   }

   @PostMapping("sprint/{sprintId}/add-item")
   public Long addItem(@PathVariable SprintId sprintId, @RequestBody AddSprintBacklogItemRequest request) {
      Sprint sprint = sprintRepo.findOneById(sprintId);
      SprintBacklogItem item = sprint.addItem(request.backlogId, request.fpEstimation);
      sprintRepo.save(sprint);
      return item.id(); // auto-flush de modificari facute pe entitati in cadrul unei tranzactii
   }

   @PostMapping("sprint/{id}/start-item/{backlogId}")
   public void startItem(@PathVariable SprintId id, @PathVariable long backlogId) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.startItem(backlogId);
   }

   @Transactional
   @PostMapping("sprint/{id}/complete-item/{backlogId}")
   public void completeItem(@PathVariable SprintId id, @PathVariable long backlogId) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.completeItem(backlogId);
   }


   @PostMapping("sprint/{id}/log-hours")
   public void logHours(@PathVariable SprintId id, @RequestBody LogHoursRequest request) {
      Sprint sprint = sprintRepo.findOneById(id);
      sprint.logHours(request.backlogId, request.hours);
   }

   @Data
   public static class SprintDto {
      public Long productId;
      public LocalDate plannedEnd;
   }



   @Data
   public static class AddSprintBacklogItemRequest {
      public long backlogId;
      public int fpEstimation;
   }

   @Data
   public static class LogHoursRequest {
      public long backlogId;
      public int hours;
   }

}
