package victor.training.ddd.agile.repo

import victor.training.ddd.agile.common.CustomJpaRepository
import victor.training.ddd.agile.entity.Sprint

interface SprintRepo : CustomJpaRepository<Sprint, Long> {
    fun findByProductId(productId: Long):List<Sprint>
}