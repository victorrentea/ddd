package victor.training.ddd.agile.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.entity.Product;
import victor.training.ddd.agile.dto.ProductDto;
import victor.training.ddd.agile.repo.ProductRepo;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;

    @PostMapping("products")
    public Long createProduct(@RequestBody ProductDto dto) {
        if (productRepo.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("Code already defined");
        }
        Product product = new Product()
                .setCode(dto.getCode())
                .setName(dto.getName())
                .setTeamMailingList(dto.getMailingList())
                .setOwnerEmail(dto.getPoEmail())
                .setOwnerName(dto.getPoName())
                .setOwnerPhone(dto.getPoPhone());
        return productRepo.save(product).getId();
    }

    @GetMapping("products/{id}")
    public ProductDto getProduct(@PathVariable long id) {
        Product product = productRepo.findOneById(id);
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setCode(product.getCode());
        dto.setMailingList(product.getTeamMailingList());
        return dto;
    }
}
