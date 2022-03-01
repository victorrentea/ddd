package victor.training.ddd.agile.domain.service;

import org.junit.jupiter.api.Test;
import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.agile.domain.model.ProductOwner;

import static org.junit.jupiter.api.Assertions.*;

class ReleaseServiceTest {

   @Test
   void createRelease() {
//      Product product = TestData.aProduct();
//      new ReleaseService().createRelease(product, spring);
   }

}

class TestData {

   public static Product aProduct() {
      return new Product("code", "name", new ProductOwner("e", "n", "p"));
   }
}