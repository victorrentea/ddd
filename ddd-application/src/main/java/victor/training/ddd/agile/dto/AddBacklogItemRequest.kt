package victor.training.ddd.agile.dto;

public class AddBacklogItemRequest {
   public long backlogId;
   public int fpEstimation;

   public AddBacklogItemRequest(long backlogId, int fpEstimation) {
      this.backlogId = backlogId;
      this.fpEstimation = fpEstimation;
   }

   public AddBacklogItemRequest() {
   }

   public long getBacklogId() {
      return this.backlogId;
   }

   public int getFpEstimation() {
      return this.fpEstimation;
   }

   public AddBacklogItemRequest setBacklogId(long backlogId) {
      this.backlogId = backlogId;
      return this;
   }

   public AddBacklogItemRequest setFpEstimation(int fpEstimation) {
      this.fpEstimation = fpEstimation;
      return this;
   }

   public boolean equals(final Object o) {
      if (o == this) return true;
      if (!(o instanceof AddBacklogItemRequest)) return false;
      final AddBacklogItemRequest other = (AddBacklogItemRequest) o;
      if (!other.canEqual((Object) this)) return false;
      if (this.getBacklogId() != other.getBacklogId()) return false;
      if (this.getFpEstimation() != other.getFpEstimation()) return false;
      return true;
   }

   protected boolean canEqual(final Object other) {
      return other instanceof AddBacklogItemRequest;
   }

   public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      final long $backlogId = this.getBacklogId();
      result = result * PRIME + (int) ($backlogId >>> 32 ^ $backlogId);
      result = result * PRIME + this.getFpEstimation();
      return result;
   }

   public String toString() {
      return "AddBacklogItemRequest(backlogId=" + this.getBacklogId() + ", fpEstimation=" + this.getFpEstimation() + ")";
   }
}