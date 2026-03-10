import java.util.*;
public class Dijkstra {
    private Graph graph;
    private int n;
    private double[][] dist;
    public Dijkstra(Graph graph) {
        this.graph = graph;
        this.n = graph.n;
        computeAllPairs();
    }
    private void computeAllPairs() {
        dist = new double[n][n];
        for (int i = 0; i < n; i++) {
            dist[i] = runFrom(i);
        }
    }
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
