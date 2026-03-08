import java.util.List;

/**
 * Printer.java
 * All formatted console output lives here.
 * Keeps Main.java clean and readable.
 */
public class Printer {

    // ANSI colour codes
    private static final String RESET  = "\u001B[0m";
    private static final String BOLD   = "\u001B[1m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN   = "\u001B[36m";
    private static final String GREEN  = "\u001B[32m";
    private static final String RED    = "\u001B[31m";
    private static final String PINK   = "\u001B[35m";
    private static final String WHITE  = "\u001B[37m";

    public static void banner() {
        System.out.println(YELLOW + BOLD);
        System.out.println("  ╔══════════════════════════════════════════════════════╗");
        System.out.println("  ║         InviteRoute — DSA Capstone Project           ║");
        System.out.println("  ║   Multi-Day Invitation Delivery Route Planner        ║");
        System.out.println("  ╠══════════════════════════════════════════════════════╣");
        System.out.println("  ║  Algorithms: Dijkstra │ DP Bitmask TSP │ Greedy      ║");
        System.out.println("  ║  Structures: Min-Heap │ Adj Matrix │ Adj List        ║");
        System.out.println("  ╚══════════════════════════════════════════════════════╝");
        System.out.println(RESET);
    }

    public static void menu() {
        System.out.println(CYAN + BOLD + "\n  ── MAIN MENU ────────────────────────────────────" + RESET);
        System.out.println("   1. Enter relatives manually");
        System.out.println("   2. Load sample data (8 relatives)");
        System.out.println("   3. View current relatives list");
        System.out.println("   4. Run Greedy Algorithm        [O(n² log n)]");
        System.out.println("   5. Run Dijkstra Algorithm      [O((V+E) log V)]");
        System.out.println("   6. Run DP Bitmask TSP          [O(n² · 2ⁿ), n≤15]");
        System.out.println("   7. Run ALL algorithms & Compare");
        System.out.println("   8. Show Distance Matrix");
        System.out.println("   9. Show Adjacency List (Graph)");
        System.out.println("   0. Exit");
        System.out.print(YELLOW + "\n  Choose option: " + RESET);
    }

    public static void sectionHeader(String title, String color) {
        String c = color.equals("yellow") ? YELLOW
                 : color.equals("cyan")   ? CYAN
                 : color.equals("green")  ? GREEN
                 : color.equals("pink")   ? PINK : WHITE;
        System.out.println("\n" + c + BOLD);
        System.out.println("  ╔══════════════════════════════════════════════════════╗");
        System.out.printf ("  ║  %-52s║%n", " " + title);
        System.out.println("  ╚══════════════════════════════════════════════════════╝");
        System.out.print(RESET);
    }

    public static void printAlgoInfo(String name, String time, String space, String desc) {
        System.out.println("  " + BOLD + "Algorithm : " + RESET + name);
        System.out.println("  " + BOLD + "Time      : " + RESET + YELLOW + time  + RESET);
        System.out.println("  " + BOLD + "Space     : " + RESET + CYAN   + space + RESET);
        System.out.println("  " + BOLD + "About     : " + RESET + desc);
    }

    public static void printItinerary(List<DayPlan> plans, String algoName) {
        if (plans == null || plans.isEmpty()) {
            System.out.println(RED + "  No itinerary generated." + RESET);
            return;
        }

        int totalDays    = plans.size();
        int totalVisits  = plans.stream().mapToInt(DayPlan::visitCount).sum();
        int totalTravel  = plans.stream().mapToInt(p -> p.totalTravelMin).sum();

        System.out.println(GREEN + BOLD);
        System.out.printf ("  ┌─ %s ──────────────────────────────────────%n", algoName);
        System.out.printf ("  │  Total Days   : %d%n", totalDays);
        System.out.printf ("  │  Total Visits : %d%n", totalVisits);
        System.out.printf ("  │  Total Travel : %d min%n", totalTravel);
        System.out.println("  └──────────────────────────────────────────────────");
        System.out.print(RESET);

        for (DayPlan plan : plans) {
            System.out.println(CYAN + BOLD +
                String.format("%n  📅  DAY %d   [%s → %s]   %d visit(s)   %d min travel",
                    plan.dayNumber, plan.startTime, plan.endTime,
                    plan.visitCount(), plan.totalTravelMin) + RESET);

            System.out.println("  " + "─".repeat(56));
            System.out.printf("  %-4s  %-18s %-10s %-10s %-10s%n",
                "#", "Relative", "Coords", "Arrive", "Depart");
            System.out.println("  " + "─".repeat(56));

            int i = 1;
            for (DayPlan.Visit v : plan.visits) {
                System.out.printf("  %-4d  %-18s %-10s %-10s %-10s%n",
                    i++,
                    truncate(v.name, 17),
                    String.format("(%.0f,%.0f)", v.x, v.y),
                    v.arrivalTime,
                    v.departureTime);
            }
            System.out.println("  " + "─".repeat(56));
            System.out.printf("  %-4s  %-18s %-10s %-10s%n",
                "🏠", "Return Home", "", plan.endTime);
        }
    }

    public static void printComparison(
            List<DayPlan> greedy,
            List<DayPlan> dijkstra,
            List<DayPlan> dp) {

        System.out.println(YELLOW + BOLD);
        System.out.println("\n  ╔══════════════════════════════════════════════════════╗");
        System.out.println("  ║              ALGORITHM COMPARISON                    ║");
        System.out.println("  ╠══════════════╦════════════╦════════════╦═════════════╣");
        System.out.println("  ║ Metric       ║  Greedy    ║  Dijkstra  ║  DP TSP     ║");
        System.out.println("  ╠══════════════╬════════════╬════════════╬═════════════╣");
        System.out.println(RESET);

        String dpDays    = dp == null ? "n/a (n>15)" : String.valueOf(dp.size());
        String dpTravel  = dp == null ? "n/a"
            : String.valueOf(dp.stream().mapToInt(p -> p.totalTravelMin).sum()) + " min";

        System.out.printf(YELLOW + "  ║ %-12s ║ %-10s ║ %-10s ║ %-11s ║%n" + RESET,
            "Days Needed",
            greedy   == null ? "error" : greedy.size(),
            dijkstra == null ? "error" : dijkstra.size(),
            dpDays);

        System.out.printf(YELLOW + "  ║ %-12s ║ %-10s ║ %-10s ║ %-11s ║%n" + RESET,
            "Travel (min)",
            greedy   == null ? "error" : greedy.stream().mapToInt(p->p.totalTravelMin).sum()+" min",
            dijkstra == null ? "error" : dijkstra.stream().mapToInt(p->p.totalTravelMin).sum()+" min",
            dpTravel);

        System.out.printf(YELLOW + "  ║ %-12s ║ %-10s ║ %-10s ║ %-11s ║%n" + RESET,
            "Complexity",
            "O(n² log n)",
            "O((V+E)lgV)",
            "O(n²·2ⁿ)");

        System.out.printf(YELLOW + "  ║ %-12s ║ %-10s ║ %-10s ║ %-11s ║%n" + RESET,
            "Optimal?",
            "No",
            "Paths yes",
            "Yes");

        System.out.println(YELLOW + "  ╚══════════════╩════════════╩════════════╩═════════════╝" + RESET);

        // Winner
        int gDays = greedy   == null ? Integer.MAX_VALUE : greedy.size();
        int dDays = dijkstra == null ? Integer.MAX_VALUE : dijkstra.size();
        int pDays = dp       == null ? Integer.MAX_VALUE : dp.size();
        int best  = Math.min(gDays, Math.min(dDays, pDays));

        System.out.println(GREEN + BOLD + "\n  🏆 BEST RESULT: " + best + " day(s) needed");
        if (gDays == best) System.out.println("     ✅ Greedy");
        if (dDays == best) System.out.println("     ✅ Dijkstra");
        if (pDays == best) System.out.println("     ✅ DP TSP");
        System.out.print(RESET);
    }

    public static void printDistanceMatrix(Point[] points, double[][] dist) {
        int n = Math.min(points.length, 9); // show max 9x9
        System.out.println(CYAN + BOLD + "\n  Distance Matrix (Euclidean)" + RESET);
        System.out.println("  (showing first " + n + " nodes)");

        // Header row
        System.out.printf("  %10s", "");
        for (int j = 0; j < n; j++)
            System.out.printf("  %8s", truncate(points[j].name.split(" ")[0], 8));
        System.out.println();

        System.out.print("  " + "─".repeat(10 + n * 10));
        System.out.println();

        for (int i = 0; i < n; i++) {
            System.out.printf("  %10s", truncate(points[i].name.split(" ")[0], 10));
            for (int j = 0; j < n; j++) {
                if (i == j) System.out.printf("  %8s", "–");
                else        System.out.printf("  %8.2f", dist[i][j]);
            }
            System.out.println();
        }
    }

    public static void printAdjacencyList(Point[] points, Graph graph) {
        System.out.println(PINK + BOLD + "\n  Adjacency List (Road Network Graph)" + RESET);
        System.out.println("  Each node → k-nearest neighbors + home");
        System.out.println("  " + "─".repeat(50));
        for (int i = 0; i < graph.n; i++) {
            System.out.printf("  %-16s → ", truncate(points[i].name, 16));
            for (int[] edge : graph.adjList[i]) {
                System.out.printf("%s(%.1f)  ",
                    truncate(points[edge[0]].name.split(" ")[0], 8),
                    graph.dist[i][edge[0]]);
            }
            System.out.println();
        }
    }

    public static void printRelatives(Point[] points) {
        if (points == null || points.length <= 1) {
            System.out.println(RED + "  No relatives added yet." + RESET);
            return;
        }
        System.out.println(WHITE + BOLD + "\n  Current Relatives:" + RESET);
        System.out.printf("  %-4s  %-20s  %-8s  %-8s%n", "#", "Name", "X", "Y");
        System.out.println("  " + "─".repeat(44));
        for (int i = 1; i < points.length; i++) {
            System.out.printf("  %-4d  %-20s  %-8.1f  %-8.1f%n",
                i, points[i].name, points[i].x, points[i].y);
        }
        System.out.printf("%n  Total: %d relative(s)%n", points.length - 1);
    }

    public static void success(String msg) {
        System.out.println(GREEN + "  ✅ " + msg + RESET);
    }

    public static void error(String msg) {
        System.out.println(RED + "  ❌ " + msg + RESET);
    }

    public static void info(String msg) {
        System.out.println(CYAN + "  ℹ  " + msg + RESET);
    }

    private static String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }
}
