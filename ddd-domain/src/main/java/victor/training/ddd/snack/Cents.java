package victor.training.ddd.snack;

import lombok.Value;

@Value
public class Cents implements Comparable<Cents> {
   int cents;

   public Cents minus(Cents x) {
      return new Cents(cents - x.cents);
   }
   public Cents add(Cents x) {
      return new Cents(cents + x.cents);
   }

   public boolean isZero() {
      return cents == 0;
   }

   @Override
   public int compareTo(Cents o) {
      return cents - o.cents;
   }
}
