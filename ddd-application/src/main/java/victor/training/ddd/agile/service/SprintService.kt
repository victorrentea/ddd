package victor.training.ddd.agile.service

import org.springframework.context.ApplicationEventPublisher
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import victor.training.ddd.agile.dto.AddBacklogItemRequest
import victor.training.ddd.agile.dto.CreateSprintRequest
import victor.training.ddd.agile.dto.LogHoursRequest
import victor.training.ddd.agile.dto.SprintMetrics
import victor.training.ddd.agile.entity.Sprint
import victor.training.ddd.agile.entity.Sprint.SprintStatus
import victor.training.ddd.agile.repo.BacklogItemRepo
import victor.training.ddd.agile.repo.ProductRepo
import victor.training.ddd.agile.repo.SprintRepo

@RestController
class SprintService(
    private val sprintRepo: SprintRepo,
    private val productRepo: ProductRepo,
    private val backlogItemRepo: BacklogItemRepo,
    private val emailService: EmailService,
    private val mailingListClient: MailingListClient,
    private val sprintMetricsService: SprintMetricsService,
    private val applicationEventPublisher: ApplicationEventPublisher
) {
    @PostMapping("sprint")
    fun createSprint(@RequestBody dto: CreateSprintRequest): Long? {
        val product = productRepo.findOneById(dto.productId)
        val sprint = Sprint(product, product.incrementAndGetIteration(), dto.plannedEnd)
        return sprintRepo.save(sprint).id
    }

    @GetMapping("sprint/{id}")
    fun getSprint(@PathVariable id: Long): Sprint {
        return sprintRepo.findOneById(id)
    }

    @PostMapping("sprint/{id}/start")
    fun startSprint(@PathVariable id: Long) {
        val sprint = sprintRepo.findOneById(id)
        sprint.start()
        sprintRepo.save(sprint) // in your case when working with BL objects.
    }

    @PostMapping("sprint/{id}/end")
    fun endSprint(@PathVariable id: Long) {
        val sprint = sprintRepo.findOneById(id)
        sprint.end()
        sprintRepo.save(sprint)
    }

    /*****************************  ITEMS IN SPRINT  */
//@Transactional
    @PostMapping("sprint/{sprintId}/add-item")
    fun addItem(@PathVariable sprintId: Long, @RequestBody request: AddBacklogItemRequest): Long? {
        val backlogItem = backlogItemRepo.findOneById(request.backlogId)
        val sprint = sprintRepo.findOneById(sprintId)
        check(sprint.status() === SprintStatus.CREATED) { "Can only add items to Sprint before it starts" }

        sprint.addItem(backlogItem, request.fpEstimation)

        sprintRepo.save(sprint)
        return backlogItem.id // Hint: if you have JPA issues getting the new ID, consider using UUID instead of sequence
    }

    @PostMapping("sprint/{id}/start-item/{backlogId}")
    fun startItem(@PathVariable id: Long, @PathVariable backlogId: Long) {
        val sprint = sprintRepo.findOneById(id)
        sprint.startItem(backlogId)
        sprintRepo.save(sprint) // the repo within should map and save Sprint + all child BacklogItems
        // DELETE THE BacklogItemRepo !!!
    }

    @PostMapping("sprint/{id}/complete-item/{backlogId}")
    @Transactional
    fun completeItem(@PathVariable id: Long, @PathVariable backlogId: Long) {
        val sprint = sprintRepo.findOneById(id)
        println("1 Before sending ")
        sprint.completeItem(backlogId)
        println("2 after sending ")
        sprintRepo.save(sprint) // listener runs here
        println("3 end of method ")
         //listener could also run here IF the method was @Transactional
    }



    @PostMapping("sprint/{id}/log-hours")
    fun logHours(@PathVariable id: Long, @RequestBody request: LogHoursRequest) {
        val sprint = sprintRepo.findOneById(id)
        sprint.logHours(request.backlogId, request.hours)
        sprintRepo.save(sprint)
    }

    @GetMapping("sprint/{id}/metrics")
    fun getSprintMetrics(@PathVariable id: Long): SprintMetrics {
        return sprintMetricsService.getSprintMetrics(id)
    }
}