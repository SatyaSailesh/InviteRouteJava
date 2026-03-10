public class TimeUtil {
    private static final int START_HOUR = 8;
    public static String format(double minutesFrom8AM) {
        int totalMins = (int) Math.round(START_HOUR * 60 + minutesFrom8AM);
        int h = (totalMins / 60) % 24;
        int m = totalMins % 60;
        String ampm   = h >= 12 ? "PM" : "AM";
        int    hDisp  = h > 12 ? h - 12 : (h == 0 ? 12 : h);
        return String.format("%d:%02d %s", hDisp, m, ampm);
    }
    public static double travelMin(double distance) {
        return (distance / 40.0) * 60.0;
    }
}
