import java.util.*;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    // Home + relatives
    private static Point[] allPoints = new Point[]{ new Point("Home", 0, 0) };

    // Cached results
    private static List<DayPlan> lastGreedy = null;
    private static List<DayPlan> lastDijkstra = null;
    private static List<DayPlan> lastDP = null;

    private static Graph graph = null;

    // Sample relatives
    private static final Point[] SAMPLE = {
    new Point("Grandma Rose", 60, 40),
    new Point("Uncle Bob", -75, 50),
    new Point("Aunt Mary", 90, -30),
    new Point("Cousin Jake", -40, -80),
    new Point("Sister Lily", 120, 20),
    new Point("Brother Tom", -95, -25),
    new Point("Nana Eliza", 35, -110),
    new Point("Uncle Frank", -60, 95),
};

    public static void main(String[] args) {

        Printer.banner();

        while (true) {

            Printer.menu();
            String choice = sc.nextLine().trim();

            switch (choice) {

                case "1":
                    addRelativesManually();
                    break;

                case "2":
                    loadSampleData();
                    break;

                case "3":
                    Printer.printRelatives(allPoints);
                    break;

                case "4":
                    runGreedy();
                    break;

                case "5":
                    runDijkstra();
                    break;

                case "6":
                    runDP();
                    break;

                case "7":
                    runAll();
                    break;

                case "8":
                    showDistanceMatrix();
                    break;

                case "9":
                    showAdjacencyList();
                    break;

                case "0":
                    System.out.println("\nGoodbye!\n");
                    return;

                default:
                    Printer.error("Invalid option. Choose 0–9.");
            }

            System.out.println("\nPress ENTER to continue...");
            sc.nextLine();
        }
    }

    private static void addRelativesManually() {

        System.out.print("How many relatives? ");

        int count;

        try {
            count = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            Printer.error("Invalid number");
            return;
        }

        List<Point> list = new ArrayList<>();
        list.add(new Point("Home", 0, 0));

        for (int i = 1; i <= count; i++) {

            System.out.println("\nRelative " + i);

            System.out.print("Name: ");
            String name = sc.nextLine();

            try {

                System.out.print("X: ");
                double x = Double.parseDouble(sc.nextLine());

                System.out.print("Y: ");
                double y = Double.parseDouble(sc.nextLine());

                list.add(new Point(name, x, y));

            } catch (Exception e) {

                Printer.error("Invalid coordinates");
                i--;
            }
        }

        allPoints = list.toArray(new Point[0]);

        graph = null;
        resetResults();
    }

    private static void loadSampleData() {

        allPoints = new Point[SAMPLE.length + 1];

        allPoints[0] = new Point("Home", 0, 0);

        System.arraycopy(SAMPLE, 0, allPoints, 1, SAMPLE.length);

        graph = null;
        resetResults();

        Printer.success("Sample data loaded");
    }

    private static void runGreedy() {

        if (!checkRelatives()) return;

        ensureGraph();

        lastGreedy = Scheduler.multiDay(allPoints, (i, j) -> graph.dist[i][j]);

        Printer.printItinerary(lastGreedy, "Greedy");
    }

    private static void runDijkstra() {

        if (!checkRelatives()) return;

        ensureGraph();

        Dijkstra dijkstra = new Dijkstra(graph);

        lastDijkstra = Scheduler.multiDay(allPoints, dijkstra::shortest);

        Printer.printItinerary(lastDijkstra, "Dijkstra");
    }

    private static void runDP() {

        if (!checkRelatives()) return;

        int n = allPoints.length - 1;

        if (n > DPTsp.MAX_N) {

            Printer.error("DP TSP works only for n ≤ " + DPTsp.MAX_N);
            return;
        }

        ensureGraph();

        lastDP = Scheduler.multiDayDP(allPoints, graph.dist);

        Printer.printItinerary(lastDP, "DP TSP");
    }

    private static void runAll() {

        if (!checkRelatives()) return;

        ensureGraph();

        lastGreedy = Scheduler.multiDay(allPoints, (i, j) -> graph.dist[i][j]);

        Dijkstra dijkstra = new Dijkstra(graph);

        lastDijkstra = Scheduler.multiDay(allPoints, dijkstra::shortest);

        int n = allPoints.length - 1;

        if (n <= DPTsp.MAX_N)
            lastDP = Scheduler.multiDayDP(allPoints, graph.dist);

        Printer.printComparison(lastGreedy, lastDijkstra, lastDP);
    }

    private static void showDistanceMatrix() {

        if (!checkRelatives()) return;

        ensureGraph();

        Printer.printDistanceMatrix(allPoints, graph.dist);
    }

    private static void showAdjacencyList() {

        if (!checkRelatives()) return;

        ensureGraph();

        Printer.printAdjacencyList(allPoints, graph);
    }

    private static void ensureGraph() {

        if (graph == null)
            graph = new Graph(allPoints);
    }

    private static boolean checkRelatives() {

        if (allPoints.length < 2) {

            Printer.error("Add relatives first.");
            return false;
        }

        return true;
    }

    private static void resetResults() {

        lastGreedy = null;
        lastDijkstra = null;
        lastDP = null;
    }
}