package victor.training.ddd.product.service;

import victor.training.ddd.product.model.Product;

import java.util.Optional;

public interface ExternalProductService { // call outside

   Optional<Product> loadProduct(String productId);
}
