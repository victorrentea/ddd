package victor.training.ddd.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DDD {
   @Retention(RetentionPolicy.RUNTIME)
   @interface AggregateRoot {
   }
   @Retention(RetentionPolicy.RUNTIME)
   @interface Entity {
   }
   @Retention(RetentionPolicy.RUNTIME)
   @interface ValueObject {
   }
}
