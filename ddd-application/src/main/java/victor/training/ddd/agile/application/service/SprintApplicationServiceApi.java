package victor.training.ddd.agile.application.service;

import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import victor.training.ddd.agile.application.dto.AddBacklogItemRequest;
import victor.training.ddd.agile.application.dto.CreateSprintRequest;
import victor.training.ddd.agile.application.dto.LogHoursRequest;
import victor.training.ddd.agile.domain.events.SprintCompletedEvent;
import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.agile.domain.model.Sprint;

import java.util.List;

public interface SprintApplicationServiceApi {
    @PostMapping("sprint")
    Long createSprint(@RequestBody CreateSprintRequest dto);

    @GetMapping("sprint/{sprintId}")
    Sprint getSprint(@PathVariable long sprintId);

    @PostMapping("sprint/{sprintId}/start")
    void startSprint(@PathVariable long sprintId);

    @PostMapping("sprint/{sprintId}/end")
    void endSprint(@PathVariable long sprintId);

    @PostMapping("sprint/{sprintId}/item")
    Long addItem(@PathVariable long sprintId, @RequestBody AddBacklogItemRequest request);

    @PostMapping("sprint/{sprintId}/item/{springItemId}/start")
    void startItem(@PathVariable long sprintId, @PathVariable long springItemId);

    @PostMapping("sprint/{sprintId}/item/{springItemId}/complete")
        // victor ❤️ this (not REST)
    void completeItem(@PathVariable long sprintId, @PathVariable long springItemId);


    //    @Order(2)
    @EventListener
    void meeeeToo(SprintCompletedEvent event);

    @PostMapping("sprint/{sprintId}/log-hours")
    void logHours(@PathVariable long sprintId, @RequestBody LogHoursRequest request);
}
