import java.util.List;
import java.util.ArrayList;

/**
 * DayPlan.java
 * Holds the complete schedule for a single day.
 */
public class DayPlan {

    public static class Visit {
        public String name;
        public double x, y;
        public String arrivalTime;
        public String departureTime;
        public int    travelMinFromPrev;

        public Visit(String name, double x, double y,
                     String arrival, String departure, int travelMin) {
            this.name               = name;
            this.x                  = x;
            this.y                  = y;
            this.arrivalTime        = arrival;
            this.departureTime      = departure;
            this.travelMinFromPrev  = travelMin;
        }
    }

    public int         dayNumber;
    public List<Visit> visits   = new ArrayList<>();
    public int         totalTravelMin;
    public String      startTime;
    public String      endTime;

    public DayPlan(int dayNumber) {
        this.dayNumber = dayNumber;
        this.startTime = "8:00 AM";
    }

    public int visitCount() { return visits.size(); }
}
