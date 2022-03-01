package victor.training.ddd.agile.common;

import org.springframework.stereotype.Service;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public @interface DDD {
   @Retention(RetentionPolicy.RUNTIME)
   @Service
   @interface ApplicationService {}
   @Retention(RetentionPolicy.RUNTIME)
   @Service
   @interface DomainService {}
   @Retention(RetentionPolicy.RUNTIME)
   @Service
   @interface Adapter {}

   @Retention(RetentionPolicy.RUNTIME)
   @interface DomainEntity {}
   @Retention(RetentionPolicy.RUNTIME)
   @interface AggregateRoot {}
   @Retention(RetentionPolicy.RUNTIME)
   @interface ValueObject {}
}
