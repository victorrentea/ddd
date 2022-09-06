package victor.training.ddd.agile.dto;

public class LogHoursRequest {
   public long backlogId;
   public int hours;

    public LogHoursRequest(long backlogId, int hours) {
        this.backlogId = backlogId;
        this.hours = hours;
    }

    public LogHoursRequest() {
    }

    public long getBacklogId() {
        return this.backlogId;
    }

    public int getHours() {
        return this.hours;
    }

    public LogHoursRequest setBacklogId(long backlogId) {
        this.backlogId = backlogId;
        return this;
    }

    public LogHoursRequest setHours(int hours) {
        this.hours = hours;
        return this;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LogHoursRequest)) return false;
        final LogHoursRequest other = (LogHoursRequest) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getBacklogId() != other.getBacklogId()) return false;
        if (this.getHours() != other.getHours()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LogHoursRequest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $backlogId = this.getBacklogId();
        result = result * PRIME + (int) ($backlogId >>> 32 ^ $backlogId);
        result = result * PRIME + this.getHours();
        return result;
    }

    public String toString() {
        return "LogHoursRequest(backlogId=" + this.getBacklogId() + ", hours=" + this.getHours() + ")";
    }
}