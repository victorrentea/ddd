package victor.training.ddd.snack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import victor.training.ddd.snack.Coins.CoinType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class VendingMachineTest {

   private VendingMachine vendingMachine = new VendingMachine(3);

   @BeforeEach
   public final void before() {
      vendingMachine.loadProducts(new SlotId(1), 10, new Cents(100));
   }


   @Test
   public void invalidSlotId() {
      assertThatThrownBy(() -> new SlotId(0))
          .hasMessageContaining(">= 1");
   }
   @Test
   public void invalidSlot() {
      assertThatThrownBy(() -> vendingMachine.buyProduct(new SlotId(100)))
          .hasMessageContaining("Invalid slot");
   }
   @Test
   public void noProductsOnSlot() {
      assertThatThrownBy(() -> vendingMachine.buyProduct(new SlotId(2)))
          .hasMessageContaining("No products");
   }
   @Test
   public void notEnoughMoney() {
      assertThatThrownBy(() -> vendingMachine.buyProduct(new SlotId(1)))
          .hasMessageContaining("Not enough money");
   }
   @Test
   public void noChange() {
      vendingMachine.enterMoney(new Coins().plus(CoinType.TWO_EUR, 1));
      assertThatThrownBy(() -> vendingMachine.buyProduct(new SlotId(1)))
          .hasMessageContaining("No change");
   }
   @Test
   public void buyOk() {
      Cents cents0 = vendingMachine.getMoneyInside().totalCents();
      vendingMachine.enterMoney(new Coins().plus(CoinType.FIFTY_CENTS, 3));

      assertThat(vendingMachine.buyProduct(new SlotId(1))).isTrue();
      Cents cents1 = vendingMachine.getMoneyInside().totalCents();
      assertThat(cents1.minus(cents0))
          .isEqualTo(vendingMachine.slot(new SlotId(1)).getPrice());
   }
}
