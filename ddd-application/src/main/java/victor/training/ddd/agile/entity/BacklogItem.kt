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
    enum class Status {
        CREATED, STARTED, DONE
    }

    @Enumerated(EnumType.STRING)
    var status = Status.CREATED

    @ManyToOne
    // ⚠ not NULL when assigned to a sprint
    var sprint: Sprint? = null
    // ⚠ not NULL when assigned to a sprint
    var fpEstimation: Int? = null

    private var hoursConsumed = 0


    fun complete() {
        check(status == Status.STARTED) { "Cannot complete an Item before starting it" }
        status = Status.DONE
    }

    fun start() {
        check(sprint!!.status == Sprint.Status.STARTED)
        check(status == Status.CREATED) { "Item already started" }
        status = Status.STARTED
    }

    fun getHoursConsumed() = hoursConsumed
    fun addHours(hours: Int) {
        hoursConsumed += hours
    }
}