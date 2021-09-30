package victor.training.ddd.snack;

import lombok.Getter;
import lombok.ToString;
import victor.training.ddd.order.model.DDD.Entity;

import java.util.Objects;

@Getter
@Entity
@ToString
public class Slot {
   private final SlotId id;
   private int products;
   private Cents price;

   public Slot(SlotId id) {
      this.id = id;
   }

   public void load(int products, Cents price) {
      if (products < 0) {
         throw new IllegalArgumentException("Quantity >= 0");
      }
      this.products = products;
      this.price = Objects.requireNonNull(price);
   }

   public boolean hasProducts() {
      return products >= 1;
   }

   public void sellProduct() {
      products --;
   }
}
