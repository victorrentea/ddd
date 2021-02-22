package victor.training.ddd.model;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

//Aggregate Root
public class BacklogItem {
   private Status status = Status.NEW;
   private List<Task> tasks = new ArrayList<>();

   public List<Task> getTasks() {
      return unmodifiableList(tasks);
   }


   public void spendHours(long taskId, int hours) {
      tasks.stream().filter(t -> t.getId() == taskId).findFirst().get().spendHours(hours);
      if (tasks.stream().mapToLong(Task::getHoursRemaining).sum() == 0) {
         status = Status.DONE;
      }
   }

   enum Status {
      NEW,
      IN_PROGRESS,
      DONE
   }


}



class BacklogItemClient {

   public void spendHours(int hoursSpent, long taskId) {
       BacklogItem item = new BacklogItem();
//       item.setHoursRemining(2L, oldValue - spentHours);
      item.spendHours(taskId,hoursSpent);
   }
}