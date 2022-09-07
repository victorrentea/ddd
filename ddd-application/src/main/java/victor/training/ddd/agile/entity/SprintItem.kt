package victor.training.ddd.agile.entity

import victor.training.ddd.agile.repo.BacklogItemRepo
import java.util.*
import javax.persistence.*

@Entity
class SprintItem(
//    @ManyToOne
//    val backlogItem: BacklogItem,
    val backlogItemId: Long,

    var fpEstimation: Int,

    private var hoursConsumed: Int = 0, // no sense until assigned to a Sprint

    @Enumerated(EnumType.STRING)
    var status: ItemStatus = ItemStatus.CREATED, // always = CREATED until added to a sprint
    @Id val id: String = UUID.randomUUID().toString(),
    ) {
    enum class ItemStatus {
        CREATED, STARTED, DONE
    }
    fun start() {
        check(status == ItemStatus.CREATED) { "Item already started" }
        status = ItemStatus.STARTED
    }

    fun complete() {
        check(status == ItemStatus.STARTED) { "Cannot complete an Item before starting it" }
//        registerEvent(SprintItemCompletedEvent(id!!)) // choreography>
        status = ItemStatus.DONE
    }

    fun isDone() = status === ItemStatus.DONE

    fun hoursConsumed() = hoursConsumed
    fun logHours(hours: Int) {
        require(hours >= 0) {"cannot substract hours"}
        check(status === ItemStatus.STARTED) { "Item not started" }
        hoursConsumed += hours
    }

}