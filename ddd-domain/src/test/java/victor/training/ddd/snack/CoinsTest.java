package victor.training.ddd.snack;

import org.junit.jupiter.api.Test;
import victor.training.ddd.snack.Coins.CoinType;

import static org.assertj.core.api.Assertions.assertThat;

class CoinsTest {

   @Test
   public void totalCents() {
      Cents total = new Coins()
          .plus(CoinType.TEN_CENTS, 1)
          .plus(CoinType.FIFTY_CENTS, 1)
          .totalCents();
      assertThat(total).isEqualTo(new Cents(60));
   }
   @Test
   public void extract1() {
      Coins coins = new Coins()
          .plus(CoinType.TEN_CENTS, 2)
          .plus(CoinType.FIFTY_CENTS, 1);
      assertThat(coins.canExtract(new Cents(10))).isTrue();
      assertThat(coins.canExtract(new Cents(60))).isTrue();
      assertThat(coins.canExtract(new Cents(70))).isTrue();
      assertThat(coins.canExtract(new Cents(30))).isFalse();
      assertThat(coins.canExtract(new Cents(0))).isTrue();
   }
}