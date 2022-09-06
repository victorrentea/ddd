package victor.training.ddd.agile.repo

import org.springframework.data.jpa.repository.JpaRepository
import victor.training.ddd.agile.entity.Release

interface ReleaseRepo : JpaRepository<Release, Long>