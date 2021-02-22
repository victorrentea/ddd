package victor.training.ddd.model;

public class Task {
   long id;
   int hoursRemaining;
//   BacklogItem parent;

   public long getId() {
      return id;
   }

   public int getHoursRemaining() {
      return hoursRemaining;
   }

   void setHoursRemaining(int hoursRemaining) {
      this.hoursRemaining = hoursRemaining;
//      parent.adjustStatus; // Option1   in parent scan al children and if sum =0 ==> status=DONE
   }

   void spendHours(int hoursSpent) {
      if (hoursSpent > hoursRemaining) {
         throw new IllegalArgumentException("Spending too much");
      }
      hoursRemaining-=hoursSpent;
   }
}
