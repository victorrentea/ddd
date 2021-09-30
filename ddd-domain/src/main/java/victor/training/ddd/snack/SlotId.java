package victor.training.ddd.snack;

import lombok.Value;

@Value
public class SlotId {
   int id;
   public SlotId(int id) {
      if (id <= 0) {
         throw new IllegalArgumentException("SlotId must be >= 1");
      }
      this.id = id;
   }
   @Override
   public String toString() {
      return "" +id;
   }
}
