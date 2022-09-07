package victor.training.ddd.agile.entity

import org.springframework.data.domain.AbstractAggregateRoot
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
    private val items: MutableList<BacklogItem> = ArrayList(),
//    @Version Long version,
    @Id @GeneratedValue var id: Long? = null
): AbstractAggregateRoot<Sprint>() {

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

    fun completeItem(backlogId: Long) {
        check(status == SprintStatus.STARTED)
        itemById(backlogId).complete()
        if (items.all { it.isDone() }) {
            registerEvent(SprintItemsFinishedEvent(id!!))
        }
    }

    fun logHours(backlogId: Long, hours: Int) {
        check(status == SprintStatus.STARTED)
        itemById(backlogId).logHours(hours)
    }

    fun items():List<BacklogItem> = items
    fun addItem(backlogItem: BacklogItem) {
        backlogItem.sprint = this
        items.add(backlogItem)
    }
}