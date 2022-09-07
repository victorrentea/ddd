package victor.training.ddd.agile.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany

@Entity
class Product(
    val code: String,
    val name: String,
    val teamMailingList: String,
    private var currentIteration: Int = 0,
    private var currentVersion: Int = 0,
    @OneToMany(mappedBy = "product")
    val releases: MutableList<Release> = ArrayList(),
    @Id
    @GeneratedValue
    var id: Long? = null
) {

    fun incrementAndGetIteration(): Int {
        currentIteration++
        return currentIteration
    }

    fun incrementAndGetVersion(): Int {
        currentVersion++
        return currentVersion
    }
}