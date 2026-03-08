import java.util.*;
/**
 * Dijkstra.java
 * ─────────────────────────────────────────────────────────────
 * DSA: Dijkstra's Single-Source Shortest Path
 *
 * Data Structures:
 *   • Adjacency List  — road-network graph
 *   • Min-Heap        — always expand minimum-cost node
 *   • dist[]          — shortest distances from source
 *   • prev[]          — path reconstruction array
 *
 * Complexity:
 *   Time  — O((V + E) log V)
 *   Space — O(V)
 *
 * Guarantee:
 *   Provably shortest path in weighted non-negative graph.
 * ─────────────────────────────────────────────────────────────
 */



public class Dijkstra {

    private Graph graph;
    private int n;

    // shortest path matrix
    private double[][] dist;

    public Dijkstra(Graph graph) {
        this.graph = graph;
        this.n = graph.n;
        computeAllPairs();
    }

    // run Dijkstra from every node
    private void computeAllPairs() {

        dist = new double[n][n];

        for (int i = 0; i < n; i++) {
            dist[i] = runFrom(i);
        }
    }

    // single-source Dijkstra
    private double[] runFrom(int src) {

        double[] distance = new double[n];
        Arrays.fill(distance, Double.MAX_VALUE);

        distance[src] = 0;

        PriorityQueue<Node> pq =
                new PriorityQueue<>((a,b)->Double.compare(a.dist,b.dist));

        pq.add(new Node(src,0));

        while(!pq.isEmpty()){

            Node curr = pq.poll();
            int u = curr.node;

            if(curr.dist > distance[u]) continue;

            for(int[] edge : graph.adjList[u]){

                int v = edge[0];

                double weight = graph.dist[u][v];

                double newDist = distance[u] + weight;

                if(newDist < distance[v]){

                    distance[v] = newDist;

                    pq.add(new Node(v,newDist));
                }
            }
        }

        return distance;
    }

    // used by Main.java
    public double shortest(int i,int j){
        return dist[i][j];
    }

    static class Node{

        int node;
        double dist;

        Node(int n,double d){
            node=n;
            dist=d;
        }
    }
}