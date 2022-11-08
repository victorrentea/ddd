package victor.training.ddd.agile.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.DDD.ApplicationService;
import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.agile.application.dto.ProductDto;
import victor.training.ddd.agile.domain.model.ProductOwner;
import victor.training.ddd.agile.domain.repo.ProductRepo;

@Slf4j
@RequiredArgsConstructor
@ApplicationService
public class ProductApplicationService /*implements ProductApi*/ {
    private final ProductRepo productRepo;

    @PutMapping("products/${productId}/poPhone/${newPhone}")
    public void updatePOPhone(long productId, String newPhone) {
        Product product = productRepo.findOneById(productId);
        product.updatePOPhone(newPhone);
    }

    // if you want @Valid on EVERY SINGLE PARAM > write a custom @Aspect invoking the spring validator
    @PostMapping("products")
    public Long createProduct(@RequestBody ProductDto dto) {
        if (productRepo.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("Code already defined");
        }
        Product product = new Product(dto.getCode(), dto.getName())
                .setTeamMailingList(dto.getMailingList())
                .setProductOwner(new ProductOwner(dto.getPoEmail(), dto.getPoName(), dto.getPoPhone()));
        return productRepo.save(product).getId();
    }

    @GetMapping("products/{id}")
    public ProductDto getProduct(@PathVariable long id) {
        Product product = productRepo.findOneById(id);
        ProductDto dto = new ProductDto()
                .setId(product.getId())
                .setName(product.getName())
                .setCode(product.getCode())
                .setMailingList(product.getTeamMailingList())
                .setPoEmail(product.getProductOwner().getOwnerEmail())
                .setPoName(product.getProductOwner().getOwnerName())
                .setPoPhone(product.getProductOwner().getOwnerPhone())
                ;
        return dto;
    }
}
