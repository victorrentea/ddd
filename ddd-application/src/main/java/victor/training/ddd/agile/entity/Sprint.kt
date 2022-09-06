package victor.training.ddd.agile.entity

import java.time.LocalDate
import javax.persistence.*

@Entity // DDD AggregateRoot

class Sprint(
    @ManyToOne
    val product: Product? = null,
    val iteration:Int = 0,
    val plannedEndDate: LocalDate? = null,
    private var startDate: LocalDate? = null,
    private var endDate: LocalDate? = null,
    @Enumerated(EnumType.STRING)
    private var status:SprintStatus = SprintStatus.CREATED,
    @OneToMany(mappedBy = "sprint")
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
        val item = items.stream()
            .filter { it: BacklogItem -> it.id == backlogId }
            .findFirst()
            .orElseThrow()
        item.start()
    }
}