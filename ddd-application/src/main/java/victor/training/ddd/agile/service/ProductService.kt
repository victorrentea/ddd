package victor.training.ddd.agile.service

import org.springframework.web.bind.annotation.*
import victor.training.ddd.agile.dto.ProductDto
import victor.training.ddd.agile.entity.Product
import victor.training.ddd.agile.repo.ProductRepo

@RestController
class ProductService(private val productRepo: ProductRepo) {

    @PostMapping("products")
    fun createProduct(@RequestBody dto: ProductDto): Long? {
        require(!productRepo.existsByCode(dto.code)) { "Code already defined" }
        val product = Product(dto.code, dto.name, dto.mailingList)
        return productRepo.save(product).id
    }

    @GetMapping("products/{id}")
    fun getProduct(@PathVariable id: Long): ProductDto {
        val product = productRepo.findOneById(id)
        return ProductDto(product.id, product.code, product.name, product.teamMailingList)
    }
}