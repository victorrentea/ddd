package victor.training.ddd.order.model;

// Aggregate

@interface DDD {
   @interface Aggregate {}
   @interface ValueObject {}
}

@DDD.Aggregate
public class Customer {
   private String id;
   private int fidelityPoints;

}
