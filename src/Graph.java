import java.util.*;

/**
 * Graph representation
 * Maintains:
 * 1) Distance matrix (Euclidean)
 * 2) Adjacency list (road network)
 */

public class Graph {

    int n;

    // distance matrix
    double[][] dist;

    // adjacency list
    List<int[]>[] adjList;

    public Graph(Point[] points) {

        n = points.length;

        dist = new double[n][n];

        adjList = new ArrayList[n];

        for (int i = 0; i < n; i++)
            adjList[i] = new ArrayList<>();


        // compute Euclidean distances
        for (int i = 0; i < n; i++) {

            for (int j = 0; j < n; j++) {

                double dx = points[i].x - points[j].x;
                double dy = points[i].y - points[j].y;

                dist[i][j] = Math.sqrt(dx * dx + dy * dy);
            }
        }


        // build adjacency list (connect every node)
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