package victor.training.ddd.agile.dto;

public class SprintMetrics {
   public int consumedHours;
   public int doneFP;
   public double fpVelocity;
   public int hoursConsumedForNotDone;
   public int calendarDays;
   public int delayDays;

    public SprintMetrics() {
    }

    public int getConsumedHours() {
        return this.consumedHours;
    }

    public int getDoneFP() {
        return this.doneFP;
    }

    public double getFpVelocity() {
        return this.fpVelocity;
    }

    public int getHoursConsumedForNotDone() {
        return this.hoursConsumedForNotDone;
    }

    public int getCalendarDays() {
        return this.calendarDays;
    }

    public int getDelayDays() {
        return this.delayDays;
    }

    public SprintMetrics setConsumedHours(int consumedHours) {
        this.consumedHours = consumedHours;
        return this;
    }

    public SprintMetrics setDoneFP(int doneFP) {
        this.doneFP = doneFP;
        return this;
    }

    public SprintMetrics setFpVelocity(double fpVelocity) {
        this.fpVelocity = fpVelocity;
        return this;
    }

    public SprintMetrics setHoursConsumedForNotDone(int hoursConsumedForNotDone) {
        this.hoursConsumedForNotDone = hoursConsumedForNotDone;
        return this;
    }

    public SprintMetrics setCalendarDays(int calendarDays) {
        this.calendarDays = calendarDays;
        return this;
    }

    public SprintMetrics setDelayDays(int delayDays) {
        this.delayDays = delayDays;
        return this;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SprintMetrics)) return false;
        final SprintMetrics other = (SprintMetrics) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getConsumedHours() != other.getConsumedHours()) return false;
        if (this.getDoneFP() != other.getDoneFP()) return false;
        if (Double.compare(this.getFpVelocity(), other.getFpVelocity()) != 0) return false;
        if (this.getHoursConsumedForNotDone() != other.getHoursConsumedForNotDone()) return false;
        if (this.getCalendarDays() != other.getCalendarDays()) return false;
        if (this.getDelayDays() != other.getDelayDays()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SprintMetrics;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getConsumedHours();
        result = result * PRIME + this.getDoneFP();
        final long $fpVelocity = Double.doubleToLongBits(this.getFpVelocity());
        result = result * PRIME + (int) ($fpVelocity >>> 32 ^ $fpVelocity);
        result = result * PRIME + this.getHoursConsumedForNotDone();
        result = result * PRIME + this.getCalendarDays();
        result = result * PRIME + this.getDelayDays();
        return result;
    }

    public String toString() {
        return "SprintMetrics(consumedHours=" + this.getConsumedHours() + ", doneFP=" + this.getDoneFP() + ", fpVelocity=" + this.getFpVelocity() + ", hoursConsumedForNotDone=" + this.getHoursConsumedForNotDone() + ", calendarDays=" + this.getCalendarDays() + ", delayDays=" + this.getDelayDays() + ")";
    }
}