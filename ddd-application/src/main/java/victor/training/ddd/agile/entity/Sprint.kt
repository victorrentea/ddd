package victor.training.ddd.agile.entity

import org.springframework.context.ApplicationEventPublisher
import victor.training.ddd.agile.common.DomainEvents
import victor.training.ddd.agile.service.EmailService
import java.time.LocalDate
import javax.persistence.*

@Entity // DDD AggregateRoot

class Sprint(
    @ManyToOne
    val product: Product,
    val iteration:Int = 0,
    val plannedEndDate: LocalDate? = null,
    private var startDate: LocalDate? = null,
    private var endDate: LocalDate? = null,
    @Enumerated(EnumType.STRING)
    private var status:SprintStatus = SprintStatus.CREATED,
    @OneToMany(mappedBy = "sprint", fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    val items: MutableList<BacklogItem> = ArrayList(),
    @Id @GeneratedValue var id: Long? = null
) {

    enum class SprintStatus {
        CREATED, STARTED, FINISHED
    }

    fun end() {
        check(status == SprintStatus.STARTED)
        endDate = LocalDate.now()
        status = SprintStatus.FINISHED
    }

    fun start() {
        check(status == SprintStatus.CREATED)
        startDate = LocalDate.now()
        status = SprintStatus.STARTED
    }

    fun status() = status
    fun startDate() = startDate
    fun endDate() = endDate

    fun startItem(backlogId: Long) {
        check(status == SprintStatus.STARTED)
        itemById(backlogId).start()
    }

    private fun itemById(backlogId: Long) = items.stream()
        .filter { it.id == backlogId }
        .findFirst()
        .orElseThrow()

    fun completeItem(backlogId: Long, applicationEventPublisher: ApplicationEventPublisher) {
        check(status == SprintStatus.STARTED)
        itemById(backlogId).complete()
        if (items.all { it.isDone() }) {
            DomainEvents.publishEvent(SprintItemsFinishedEvent(id!!))
//            applicationEventPublisher.publishEvent(SprintItemsFinishedEvent(id!!))

            SprintItemsFinishedEvent(id!!)
        // passing the entire BL inside an Event is A BAD practice if the BL object is mutable !


//            emailService.sendCongratsEmail(product.code, product.teamMailingList) // couping me to the EmailService  / or a door of another module
        }
    }

    fun logHours(backlogId: Long, hours: Int) {
        check(status == SprintStatus.STARTED)
        itemById(backlogId).logHours(hours)
    }
}