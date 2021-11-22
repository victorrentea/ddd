package victor.training.ddd.product.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserStory { // aggregate root - respo: consistenta intregului agregate(UserStory + Task + TaskDescription)
   enum Status {
      IN_PROGRESS,
      DONE
   }

   //   @Embedded
//   private TaskDescription taskDescription;
   @Id
   @GeneratedValue
   private Long id;
   private Status status = Status.IN_PROGRESS;
   @OneToMany(fetch = FetchType.EAGER)
   @JoinColumn
   private List<Task> tasks = new ArrayList<>();

   private void checkFinishedChildren() {
      if (tasks.stream().allMatch(Task::isDone)) {
         status = Status.DONE;
      }
   }

   public void finishSubTask(Long taskId, String user) {
      tasks.stream().filter(t -> t.getId().equals(taskId)).findFirst().get().finish(user);
      checkFinishedChildren();
   }

}

@Entity
class Task {
   enum Status {
      DRAFT,
      IN_PROGRESS,
      DONE
   }

   @Id
   @GeneratedValue
   private Long id;
//   private Long taskHumanId;
   private Status status = Status.DRAFT;
   private String finishedByUser;
   private String comment;
//   private UserStory parent; // solutia 1


   public String getComment() {
      return comment;
   }

   public Task setComment(String comment) {
      this.comment = comment;
      return this;
   }

   public Long getId() {
      return id;
   }

   public Status getStatus() {
      return status;
   }

   public boolean isDone() {
      return status == Status.DONE;
   }

   void finish(String user) {
      if (status != Status.IN_PROGRESS) {
         throw new IllegalStateException();
      }
      finishedByUser = user;
      status = Status.DONE;
      // parent.finishedChildren()// solutia 1
   }
}