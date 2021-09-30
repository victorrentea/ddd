package victor.training.ddd.snack;

import lombok.Getter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import victor.training.ddd.order.model.DDD.Aggregate;

import java.util.ArrayList;
import java.util.List;

@Aggregate
@Getter
@ToString
public class VendingMachine {
   @Id
   private ObjectId id;

   private List<Slot> slots = new ArrayList<>();

   private Coins moneyInside = new Coins();
   private Coins currentTransaction = new Coins();

   private VendingMachine() {}
   public VendingMachine(int slotsNumber) {
      for (int i = 0; i < slotsNumber; i++) {
         slots.add(new Slot(new SlotId(i + 1)));
      }
   }

   public void loadProducts(SlotId slotId, int products, Cents price) {
      slot(slotId).load(products, price);
   }

   public Slot slot(SlotId slotId) {
      return slots.stream().filter(slot -> slot.getId().equals(slotId))
          .findFirst()
          .orElseThrow(() -> new IllegalArgumentException("Invalid slot: " +slotId));
   }

   public void enterMoney(Coins coins) {
      currentTransaction = currentTransaction.plus(coins);
   }

   public boolean buyProduct(SlotId slotId) {
      Slot slot = slot(slotId);

      if (!slot.hasProducts()) {
         throw new IllegalStateException("No products on slot "+ slotId);
      }
      if (currentTransaction.totalCents().compareTo(slot.getPrice()) < 0) {
         throw new IllegalStateException("Not enough money");
      }
      Cents change = currentTransaction.totalCents().minus(slot.getPrice());
      if (!moneyInside.plus(currentTransaction).canExtract(change)) {
         throw new IllegalStateException("No change");
      }

      slot.sellProduct();
      moneyInside = moneyInside.plus(currentTransaction);
      currentTransaction = new Coins();
      Coins changeCoins = moneyInside.tryExtract(change);
      outputCoins(changeCoins);
      moneyInside = moneyInside.minus(changeCoins);
      return true;
   }

   private void outputCoins(Coins coins) {
      System.out.println("Output change: " + coins);
   }
}
