package victor.training.ddd.agile.entity

import javax.persistence.*
import javax.validation.constraints.NotNull

// Child entity of the "Sprint" Aggregate.
@Entity
class BacklogItem(
    @field:ManyToOne val product: Product,
    val title: @NotNull String?,
    val description: String,
    @Version val version: Long?,
    @GeneratedValue @Id val id: Long?
) {
    enum class ItemStatus {
        CREATED, STARTED, DONE
    }

    @Enumerated(EnumType.STRING)
    var status = ItemStatus.CREATED

    @ManyToOne
    // ⚠ not NULL when assigned to a sprint
    var sprint: Sprint? = null
    // ⚠ not NULL when assigned to a sprint
    var fpEstimation: Int? = null

    private var hoursConsumed = 0


    fun complete() {
        check(status == ItemStatus.STARTED) { "Cannot complete an Item before starting it" }
        status = ItemStatus.DONE
    }

    fun start() {
        check(sprint!!.status() == Sprint.SprintStatus.STARTED)
        check(status == ItemStatus.CREATED) { "Item already started" }
        status = ItemStatus.STARTED
    }

    fun getHoursConsumed() = hoursConsumed
    fun addHours(hours: Int) {
        hoursConsumed += hours
    }
}