package victor.training.ddd.agile.entity

import org.springframework.data.domain.AbstractAggregateRoot
import java.time.LocalDate
import javax.persistence.*

@Entity // DDD AggregateRoot

class Sprint(
//    @ManyToOne
//    val product: Product,
    val productId: Long,
    val iteration:Int = 0,
    val plannedEndDate: LocalDate? = null,
    private var startDate: LocalDate? = null,
    private var endDate: LocalDate? = null,
    @Enumerated(EnumType.STRING)
    private var status:SprintStatus = SprintStatus.CREATED,
    @JoinColumn
    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    private val items: MutableList<SprintItem> = ArrayList(),
//    @Version Long version,
    @Id @GeneratedValue var id: Long? = null
): AbstractAggregateRoot<Sprint>() {

    enum class SprintStatus {
        CREATED, STARTED, FINISHED
    }

    fun items() = items
    fun addItem(backlogItem: BacklogItem, fpEstimation: Int): SprintItem {
        require(backlogItem.product.id == productId)
        check(status === SprintStatus.CREATED) { "Can only add items to Sprint before it starts" }
        val sprintItem = SprintItem(backlogItem.id!!,  fpEstimation)
        items.add(sprintItem)
        return sprintItem
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

    fun startItem(sprintItemId: String) {
        check(status == SprintStatus.STARTED)
        itemById(sprintItemId).start()
    }

    private fun itemById(sprintItemId: String) = items.stream()
        .filter { it.id == sprintItemId }
        .findFirst()
        .orElseThrow()

    fun completeItem(sprintItemId: String) {
        check(status == SprintStatus.STARTED)
        itemById(sprintItemId).complete()
        if (items.all { it.isDone() }) {
            registerEvent(SprintItemsFinishedEvent(id!!))
        }
    }

    fun logHours(sprintItemId: String, hours: Int) {
        check(status == SprintStatus.STARTED)
        itemById(sprintItemId).logHours(hours)
    }


}