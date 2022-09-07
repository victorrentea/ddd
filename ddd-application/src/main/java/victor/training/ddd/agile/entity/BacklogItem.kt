package victor.training.ddd.agile.entity

import javax.persistence.*
import javax.validation.constraints.NotNull

// Child entity of the "Sprint" Aggregate.
@Entity
class BacklogItem(
    @ManyToOne val product: Product,
    val title: @NotNull String?,
    val description: String,
    @Version val version: Long? = null,
    @GeneratedValue @Id val id: Long? = null,
    @Enumerated(EnumType.STRING)
    var status: ItemStatus = ItemStatus.CREATED,
    @ManyToOne
    var sprint: Sprint? = null, // ⚠ not NULL when assigned to a sprint
    var fpEstimation: Int? = null, // ⚠ not NULL when assigned to a sprint
    private var hoursConsumed: Int = 0
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
        status = ItemStatus.DONE
    }

    fun hoursConsumed() = hoursConsumed
    fun logHours(hours: Int) {
        require(hours >= 0) {"cannot substract hours"}
        check(status === ItemStatus.STARTED) { "Item not started" }
        hoursConsumed += hours
    }

    fun isDone() = status === ItemStatus.DONE
}