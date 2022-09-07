package victor.training.ddd.agile.entity

import javax.persistence.*
import javax.validation.constraints.NotNull

// Child entity of the "Sprint" Aggregate.
@Entity
class BacklogItem(
    @ManyToOne val product: Product, // at least an id because the BI is linked to the project BEFORE it is asigned to a Spring

    var title: String,
    var description: String,

    @Version val version: Long? = null,

    @GeneratedValue @Id val id: Long? = null,

    var frozen: Boolean =false,

  ) {


//    fun isDone() = status === ItemStatus.DONE
}