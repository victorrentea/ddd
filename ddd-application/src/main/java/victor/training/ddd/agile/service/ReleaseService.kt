package victor.training.ddd.agile.service

import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import victor.training.ddd.agile.entity.Release
import victor.training.ddd.agile.entity.Sprint
import victor.training.ddd.agile.entity.SprintItemsFinishedEvent
import victor.training.ddd.agile.repo.ProductRepo
import victor.training.ddd.agile.repo.ReleaseRepo
import victor.training.ddd.agile.repo.SprintRepo
import java.time.LocalDate
import java.util.stream.Collectors

@Transactional
@RestController
@Validated // needed as tests call directly these methods
class ReleaseService(
    private val releaseRepo: ReleaseRepo,
    private val productRepo: ProductRepo,
    private val sprintRepo: SprintRepo
) {

    @EventListener
    @Order(100)
    fun meetoButBeforeSendingEmails(sprintItemsFinishedEvent: SprintItemsFinishedEvent) {
        println("Meeee too")
    }

    @PostMapping("product/{productId}/release/{sprintId}")
    fun createRelease(@PathVariable productId: Long, @PathVariable sprintId: Long): Release {
        val product = productRepo.findOneById(productId)
        val sprint = sprintRepo.findOneById(sprintId)
        val previouslyReleasedIteration = product.releases.stream()
            .map(Release::sprint)
            .mapToInt { obj: Sprint -> obj.iteration }
            .max().orElse(0)
        val releasedIteration = sprint.iteration
        val releasedItems = product.sprints.stream()
            .sorted(Comparator.comparing { obj: Sprint -> obj.iteration })
            .filter { s: Sprint ->
                (s.iteration in (previouslyReleasedIteration + 1)..releasedIteration)
            }
            .flatMap { s: Sprint -> s.items.stream() }
            .collect(Collectors.toList())
        val release = Release(
            product,
            sprint,
            releasedItems,
            LocalDate.now(),
            product.incrementAndGetVersion().toString() + ".0", null
        )
        product.releases.add(release)
        releaseRepo.save(release)
        return release
    }
}