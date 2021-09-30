package victor.training.ddd.snack;

import lombok.Value;
import org.springframework.data.annotation.PersistenceConstructor;
import victor.training.ddd.order.model.DDD.ValueObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@ValueObject
@Value
public class Coins {

   enum CoinType {
      TEN_CENTS(10),
      FIFTY_CENTS(50),
      ONE_EUR(100),
      TWO_EUR(200);

      private final int cents;
      CoinType(int cents) {
         this.cents = cents;
      }
      public int cents() {
         return cents;
      }
   }
   Map<CoinType, Integer> coins;

   public Coins() {
      this.coins = new HashMap<>();
      Stream.of(CoinType.values()).forEach(t -> coins.put(t, 0));
   }

   @PersistenceConstructor
   private Coins(Map<CoinType, Integer> coins) {
      this();
      this.coins.putAll(coins);
   }
   public Coins plus(CoinType type, int number) {
      HashMap<CoinType, Integer> newMap = new HashMap<>(coins);
      newMap.compute(type, (t,c) -> c + number);
      return new Coins(newMap);
   }

   public Coins minus(Coins x) {
      Map<CoinType, Integer> negateMap = x.coins.entrySet().stream().collect(toMap(Entry::getKey, k -> -1 * k.getValue()));
      return plus(new Coins(negateMap));
   }

   public Coins plus(Coins coins) {
      Coins result = this;
      for (CoinType coinType : coins.coins.keySet()) {
         result = result.plus(coinType, coins.getCoins().get(coinType));
      }
      return result;
   }
   public Cents totalCents() {
      return new Cents(coins.entrySet().stream()
          .mapToInt(e -> e.getKey().cents * e.getValue())
          .sum());
   }


   public Coins tryExtract(Cents cents) {
      List<CoinType> coinsBigToSmall = Stream.of(CoinType.values()).sorted(comparing(CoinType::cents).reversed()).collect(toList());

      Coins result = new Coins();
      Cents remaining = cents;
      for (CoinType coinType : coinsBigToSmall) {
         int maxCoinsOfThisType = remaining.getCents() / coinType.cents();
         int availableCoins = Math.min(coins.get(coinType), maxCoinsOfThisType);
         remaining = remaining.minus(new Cents(availableCoins * coinType.cents()));
         result = result.plus(coinType, availableCoins);
      }
      return result;
   }

   public boolean canExtract(Cents cents) {
      return tryExtract(cents).totalCents().equals(cents);
   }
}
