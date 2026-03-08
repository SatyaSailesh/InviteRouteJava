import java.util.*;

/**
 * Scheduler.java
 * ─────────────────────────────────────────────────────────────
 * Core multi-day scheduling engine.
 *
 * Given a distance function (Euclidean OR Dijkstra road-network),
 * packs as many relatives as possible each day within the
 * 840-minute budget, then starts a new day for the rest.
 *
 * Strategy: Greedy nearest-neighbor on the given distFn.
 * This is shared by all three algorithm modes.
 * ─────────────────────────────────────────────────────────────
 */
public class Scheduler {

    private static final int    VISIT_MIN    = 15;
    private static final double DAY_BUDGET   = 840.0; // 8AM–10PM

    /**
     * Build multi-day itinerary.
     * @param allPoints full array (index 0 = home)
     * @param distFn    distance oracle — Euclidean or Dijkstra shortest
     */
    public static List<DayPlan> multiDay(Point[] allPoints,
                                         Greedy.DistanceFunction distFn) {
        int n = allPoints.length;
        boolean[] visited = new boolean[n];
        visited[0] = true; // home is not a relative to visit

        List<DayPlan> plans = new ArrayList<>();
        int day = 1;

        while (hasUnvisited(visited, n)) {
            DayPlan plan = new DayPlan(day++);
            double elapsed = 0;
            int current = 0;

            while (true) {
                // Build min-heap of all feasible unvisited relatives
                MinHeap pq = new MinHeap(n);
                for (int i = 1; i < n; i++) {
                    if (visited[i]) continue;
                    double travelToI   = TimeUtil.travelMin(distFn.distance(current, i));
                    double travelHome  = TimeUtil.travelMin(distFn.distance(i, 0));
                    double totalIfVisit = elapsed + travelToI + VISIT_MIN + travelHome;

                    if (totalIfVisit <= DAY_BUDGET) {
                        pq.push(i, distFn.distance(current, i));
                    }
                }

                if (pq.isEmpty()) break; // no more relatives fit today

                MinHeap.Entry best     = pq.pop(); // nearest feasible
                int           next     = best.node;
                double        travelMin = TimeUtil.travelMin(distFn.distance(current, next));

                elapsed += travelMin;
                String arrival   = TimeUtil.format(elapsed);
                elapsed += VISIT_MIN;
                String departure = TimeUtil.format(elapsed);

                plan.visits.add(new DayPlan.Visit(
                    allPoints[next].name,
                    allPoints[next].x,
                    allPoints[next].y,
                    arrival, departure,
                    (int) Math.round(travelMin)
                ));

                visited[next] = true;
                current = next;
            }

            // Edge case: if nothing was scheduled (relative unreachable in a day)
            // force-add the closest unvisited relative
            if (plan.visits.isEmpty()) {
                int closest = -1;
                double minD = Double.MAX_VALUE;
                for (int i = 1; i < n; i++) {
                    if (visited[i]) continue;
                    double d = distFn.distance(0, i);
                    if (d < minD) { minD = d; closest = i; }
                }
                if (closest != -1) {
                    double tM = TimeUtil.travelMin(minD);
                    plan.visits.add(new DayPlan.Visit(
                        allPoints[closest].name,
                        allPoints[closest].x,
                        allPoints[closest].y,
                        TimeUtil.format(tM),
                        TimeUtil.format(tM + VISIT_MIN),
                        (int) Math.round(tM)
                    ));
                    visited[closest] = true;
                    current = closest;
                }
            }

            // Compute return travel
            DayPlan.Visit last = plan.visits.get(plan.visits.size() - 1);
            double returnMin = TimeUtil.travelMin(
                distFn.distance(indexOf(allPoints, last.name), 0));
            // parse last departure back to minutes-from-8AM
            double endElapsed = parseToMinutes(last.departureTime) + returnMin;

            plan.totalTravelMin = plan.visits.stream()
                .mapToInt(v -> v.travelMinFromPrev).sum() + (int) Math.round(returnMin);
            plan.endTime = TimeUtil.format(endElapsed);

            plans.add(plan);
        }

        return plans;
    }

    /** Multi-day using DP TSP for per-day ordering */
    public static List<DayPlan> multiDayDP(Point[] allPoints, double[][] distMatrix) {
        int n = allPoints.length;
        boolean[] visited = new boolean[n];
        visited[0] = true;

        List<DayPlan> plans = new ArrayList<>();
        int day = 1;

        while (hasUnvisited(visited, n)) {
            // Collect unvisited
            List<Integer> unvisited = new ArrayList<>();
            for (int i = 1; i < n; i++) if (!visited[i]) unvisited.add(i);

            // Binary search: find max subset that fits in a day
            int lo = 1, hi = Math.min(unvisited.size(), DPTsp.MAX_N);
            int[] bestPath = null;
            int   bestCount = 0;

            for (int count = 1; count <= hi; count++) {
                int[] subset = unvisited.subList(0, count)
                    .stream().mapToInt(Integer::intValue).toArray();

                DPTsp tsp = new DPTsp(distMatrix, subset);
                int[] path = tsp.solve(subset);

                // Validate time
                double elapsed = 0;
                boolean fits = true;
                for (int pi = 1; pi < path.length - 1; pi++) {
                    elapsed += TimeUtil.travelMin(distMatrix[path[pi-1]][path[pi]]);
                    elapsed += 15;
                }
                elapsed += TimeUtil.travelMin(distMatrix[path[path.length-2]][0]);

                if (elapsed <= 840) {
                    bestPath  = path;
                    bestCount = count;
                } else {
                    break;
                }
            }

            if (bestPath == null || bestCount == 0) {
                // Force add closest
                int closest = unvisited.get(0);
                bestPath  = new int[]{0, closest, 0};
                bestCount = 1;
            }

            DayPlan plan   = new DayPlan(day++);
            double elapsed = 0;

            for (int pi = 1; pi < bestPath.length - 1; pi++) {
                int from = bestPath[pi - 1];
                int to   = bestPath[pi];
                double tM = TimeUtil.travelMin(distMatrix[from][to]);
                elapsed += tM;
                String arrival   = TimeUtil.format(elapsed);
                elapsed += 15;
                String departure = TimeUtil.format(elapsed);

                plan.visits.add(new DayPlan.Visit(
                    allPoints[to].name,
                    allPoints[to].x, allPoints[to].y,
                    arrival, departure,
                    (int) Math.round(tM)
                ));
                visited[to] = true;
            }

            int lastIdx = bestPath[bestPath.length - 2];
            double returnMin = TimeUtil.travelMin(distMatrix[lastIdx][0]);
            DayPlan.Visit last = plan.visits.get(plan.visits.size() - 1);
            double endElapsed  = parseToMinutes(last.departureTime) + returnMin;

            plan.totalTravelMin = plan.visits.stream()
                .mapToInt(v -> v.travelMinFromPrev).sum() + (int) Math.round(returnMin);
            plan.endTime = TimeUtil.format(endElapsed);
            plans.add(plan);
        }

        return plans;
    }

    // ── Helpers ──────────────────────────────────────────────

    private static boolean hasUnvisited(boolean[] visited, int n) {
        for (int i = 1; i < n; i++) if (!visited[i]) return true;
        return false;
    }

    private static int indexOf(Point[] pts, String name) {
        for (int i = 0; i < pts.length; i++)
            if (pts[i].name.equals(name)) return i;
        return 0;
    }

    private static double parseToMinutes(String timeStr) {
        // "H:MM AM/PM" → minutes from 8 AM
        String[] parts = timeStr.split(" ");
        String[] hm    = parts[0].split(":");
        int h = Integer.parseInt(hm[0]);
        int m = Integer.parseInt(hm[1]);
        if (parts[1].equals("PM") && h != 12) h += 12;
        if (parts[1].equals("AM") && h == 12) h = 0;
        return (h - 8) * 60.0 + m;
    }
}
