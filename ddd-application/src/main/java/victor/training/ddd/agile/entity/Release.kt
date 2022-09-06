package victor.training.ddd.agile.entity

import java.time.LocalDate
import java.util.stream.Collectors
import javax.persistence.*

@Entity
class Release(
    @ManyToOne val product: Product,
    @ManyToOne val sprint: Sprint, // only used for release notes
    @JoinColumn @OneToMany private val releasedItems: List<BacklogItem>,
    val date: LocalDate, // eg 1.0, 2.0 ...
    val version: String,
    @Id
    @GeneratedValue
    val id: Long? = null
) {
    fun getReleaseNotes():String = releasedItems.stream().map { obj: BacklogItem -> obj.title }.collect(Collectors.joining("\n"))
}