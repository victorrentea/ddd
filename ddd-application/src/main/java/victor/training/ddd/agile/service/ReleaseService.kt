package victor.training.ddd.agile.service

import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import victor.training.ddd.agile.entity.Release
import victor.training.ddd.agile.entity.Sprint
import victor.training.ddd.agile.repo.*
import java.time.LocalDate
import java.util.stream.Collectors



@Transactional
@RestController
@Validated // needed as tests call directly these methods
class ReleaseService(
    private val releaseRepo: ReleaseRepo,
    private val productRepo: ProductRepo,
    private val sprintRepo: SprintRepo,
    private val backlogItemRepo: BacklogItemRepo
) {


    @PostMapping("product/{productId}/release/{sprintId}")
    fun createRelease(@PathVariable productId: Long, @PathVariable sprintId: Long): Release {
        val product = productRepo.findOneById(productId)
        val sprint = sprintRepo.findOneById(sprintId)
        val previouslyReleasedIteration = product.releases.stream()
            .map(Release::sprint)
            .mapToInt { obj: Sprint -> obj.iteration }
            .max().orElse(0)
        val releasedIteration = sprint.iteration

        val sprints = sprintRepo.findByProductId(product.id!!)
        val releasedSprintItems = sprints.stream()
            .sorted(Comparator.comparing { obj: Sprint -> obj.iteration })
            .filter { s: Sprint ->
                (s.iteration in (previouslyReleasedIteration + 1)..releasedIteration)
            }
            .flatMap { s: Sprint -> s.items().stream() }
            .collect(Collectors.toList())

        val backlogItems = backlogItemRepo.findAllById(releasedSprintItems.map { it.backlogItemId })

        val releaseNotes = backlogItems.joinToString("\n") { it.title }

        val release = Release(
            product,
            sprint,
            releaseNotes,
            LocalDate.now(),
            product.incrementAndGetVersion().toString() + ".0", null
        )
        product.releases.add(release)
        releaseRepo.save(release)
        return release
    }
}