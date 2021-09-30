package victor.training.ddd.order.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public @interface DDD {
   @Retention(RetentionPolicy.RUNTIME)
   @Document
   @interface Aggregate {
   }

   @interface ValueObject {
   }

   @interface Entity {
   }
}
