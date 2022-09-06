package victor.training.ddd.agile.repo

import victor.training.ddd.agile.common.CustomJpaRepository
import victor.training.ddd.agile.entity.Product

interface ProductRepo : CustomJpaRepository<Product, Long> {
    fun existsByCode(code: String): Boolean
}
