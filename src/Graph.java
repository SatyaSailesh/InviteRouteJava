import java.util.*;
public class Graph {
    int n;
    double[][] dist;
    List<int[]>[] adjList;
    public Graph(Point[] points) {
        n = points.length;
        dist = new double[n][n];
        adjList = new ArrayList[n];
        for (int i = 0; i < n; i++)
            adjList[i] = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double dx = points[i].x - points[j].x;
                double dy = points[i].y - points[j].y;
                dist[i][j] = Math.sqrt(dx * dx + dy * dy);
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                addEdge(i, j);
            }
        }
    }
    public void addEdge(int u, int v) {
        adjList[u].add(new int[]{v});
        adjList[v].add(new int[]{u});
    }
}
