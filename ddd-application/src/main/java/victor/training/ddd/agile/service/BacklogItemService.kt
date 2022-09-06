package victor.training.ddd.agile.service

import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import victor.training.ddd.agile.dto.BacklogItemDto
import victor.training.ddd.agile.entity.BacklogItem
import victor.training.ddd.agile.repo.BacklogItemRepo
import victor.training.ddd.agile.repo.ProductRepo

@RestController
class BacklogItemService(private val backlogItemRepo: BacklogItemRepo,
                         private val productRepo: ProductRepo) {
    @PostMapping("backlog")
    @Transactional
    fun createBacklogItem(@RequestBody dto: BacklogItemDto): Long? {
        val product = productRepo.findOneById(dto.productId)
        val backlogItem = BacklogItem(product, dto.description, dto.title)
        return backlogItemRepo.save(backlogItem).id
    }

    @GetMapping("backlog/{id}")
    fun getBacklogItem(@PathVariable id: Long): BacklogItemDto {
        val backlogItem = backlogItemRepo.findOneById(id)
        val dto = BacklogItemDto(backlogItem.product.id!!, backlogItem.title!!, backlogItem.description)
        dto.id = backlogItem.id
        dto.version = backlogItem.version
        return dto
    }

    @PutMapping("backlog")
    fun updateBacklogItem(@RequestBody dto: BacklogItemDto) {
        // TODO if Backlog Item is COMPLETED, reject the update
        val backlogItem = BacklogItem(
            productRepo.findOneById(dto.productId),
            dto.description,
            dto.title,
            dto.version,
            dto.id
        )
        backlogItemRepo.save(backlogItem)
    }

    @DeleteMapping("backlog/{id}")
    fun deleteBacklogItem(@PathVariable id: Long) {
        backlogItemRepo.deleteById(id)
    }
}