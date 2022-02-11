package victor.training.ddd.agile.web;

import victor.training.ddd.common.Mapper;
import victor.training.ddd.agile.web.ProductController.ProductDto;
import victor.training.ddd.agile.domain.model.Product;

@Mapper
public class ProductMapper {
   //   Repo repo;
   public Product toEntity(ProductDto dto) { // noble goal to keep mapping static < doable if DDD more aggresively
      Product product = new Product(
          dto.code,
          dto.name,
          dto.mailingList); // in readlity have 10 params constructor
      // BREAK THE MODEL FURHTER. MORE VALUE OBJECT 10 > 7
      return product;
   }
}
