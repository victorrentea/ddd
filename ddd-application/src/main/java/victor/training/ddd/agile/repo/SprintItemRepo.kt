package victor.training.ddd.agile.repo

import victor.training.ddd.agile.common.CustomJpaRepository
import victor.training.ddd.agile.entity.BacklogItem
import victor.training.ddd.agile.entity.SprintItem

interface SprintItemRepo : CustomJpaRepository<SprintItem  , Long>