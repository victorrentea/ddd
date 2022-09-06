package victor.training.ddd.agile.service

import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import victor.training.ddd.agile.dto.AddBacklogItemRequest
import victor.training.ddd.agile.dto.CreateSprintRequest
import victor.training.ddd.agile.dto.LogHoursRequest
import victor.training.ddd.agile.dto.SprintMetrics
import victor.training.ddd.agile.entity.BacklogItem
import victor.training.ddd.agile.entity.Sprint
import victor.training.ddd.agile.entity.Sprint.SprintStatus
import victor.training.ddd.agile.repo.BacklogItemRepo
import victor.training.ddd.agile.repo.ProductRepo
import victor.training.ddd.agile.repo.SprintRepo

@Transactional
@RestController
class SprintService(
    private val sprintRepo: SprintRepo,
    private val productRepo: ProductRepo,
    private val backlogItemRepo: BacklogItemRepo,
    private val emailService: EmailService,
    private val mailingListClient: MailingListClient,
    private val sprintMetricsService: SprintMetricsService
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
    }

    /*****************************  ITEMS IN SPRINT  */
    @PostMapping("sprint/{sprintId}/add-item")
    fun addItem(@PathVariable sprintId: Long, @RequestBody request: AddBacklogItemRequest): Long? {
        val backlogItem = backlogItemRepo.findOneById(request.backlogId)
        val sprint = sprintRepo.findOneById(sprintId)
        check(!(sprint.status() !== SprintStatus.CREATED)) { "Can only add items to Sprint before it starts" }
        backlogItem.sprint = sprint
        sprint.items.add(backlogItem)
        backlogItem.fpEstimation = request.fpEstimation
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
    fun completeItem(@PathVariable id: Long, @PathVariable backlogId: Long) {
        val backlogItem = backlogItemRepo.findOneById(backlogId)
        checkSprintMatchesAndStarted(id, backlogItem)
        backlogItem.complete()
        val sprint = sprintRepo.findOneById(id)
        if (sprint.items.stream().allMatch { item: BacklogItem -> item.status === BacklogItem.ItemStatus.DONE }) {
            println("Sending CONGRATS email to team of product " + sprint.product!!.code + ": They finished the items earlier. They have time to refactor! (OMG!)")
            val emails: List<String?> = mailingListClient.retrieveEmails(sprint.product.teamMailingList)
            emailService.sendCongratsEmail(emails)
        }
    }

    private fun checkSprintMatchesAndStarted(id: Long, backlogItem: BacklogItem) {
        require(backlogItem.sprint!!.id == id) { "item not in sprint" }
        val sprint = sprintRepo.findOneById(id)
        check(sprint.status() === SprintStatus.STARTED) { "Sprint not started" }
    }

    @PostMapping("sprint/{id}/log-hours")
    fun logHours(@PathVariable id: Long, @RequestBody request: LogHoursRequest) {
        val backlogItem = backlogItemRepo.findOneById(request.backlogId)
        checkSprintMatchesAndStarted(id, backlogItem)
        check(!(backlogItem.status !== BacklogItem.ItemStatus.STARTED)) { "Item not started" }
        backlogItem.addHours(request.hours)
    }

    @GetMapping("sprint/{id}/metrics")
    fun getSprintMetrics(@PathVariable id: Long): SprintMetrics {
        return sprintMetricsService.getSprintMetrics(id)
    }
}