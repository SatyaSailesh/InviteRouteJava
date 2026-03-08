import java.util.ArrayList;
import java.util.List;

/**
 * DPTsp.java
 * ─────────────────────────────────────────────────────────────
 * DSA: Held-Karp DP Bitmask TSP — Exact Optimal Solution
 *
 * State:
 *   dp[mask][i] = minimum cost to visit all nodes in "mask",
 *                 currently at node i, starting from home (0)
 *
 * Transition:
 *   dp[mask | (1<<v)][v] = min(dp[mask][i] + dist[i][v])
 *   for every unvisited v not in mask
 *
 * Answer:
 *   min over all last nodes u of: dp[fullMask][u] + dist[u][0]
 *
 * Complexity:
 *   Time  — O(n² · 2ⁿ)
 *   Space — O(n · 2ⁿ)
 *   Feasible only for n ≤ 15 nodes
 * ─────────────────────────────────────────────────────────────
 */
public class DPTsp {

    public static final int  MAX_N     = 15;
    public static final double INF     = Double.MAX_VALUE / 2;

    private final double[][] dist;    // distance matrix for subset
    private final int        n;       // number of nodes in subset (incl. home)

    private double[][] dp;
    private int[][]    parent;

    public DPTsp(double[][] distMatrix, int[] nodeSubset) {
        // nodeSubset = relative indices; we prepend home (index 0)
        this.n    = nodeSubset.length + 1;
        this.dist = new double[n][n];

        // remap: local index 0 = global 0 (home)
        int[] nodes = new int[n];
        nodes[0] = 0;
        for (int i = 0; i < nodeSubset.length; i++) nodes[i + 1] = nodeSubset[i];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                this.dist[i][j] = distMatrix[nodes[i]][nodes[j]];
    }

    /**
     * Solve TSP for the given subset.
     * Returns the ordered list of GLOBAL node indices (path),
     * including return to home at the end.
     */
    public int[] solve(int[] nodeSubset) {
        int states = 1 << n;
        dp     = new double[states][n];
        parent = new int[states][n];

        for (double[] row : dp)     java.util.Arrays.fill(row, INF);
        for (int[]   row : parent)  java.util.Arrays.fill(row, -1);

        dp[1][0] = 0.0;   // visited only home, cost = 0

        for (int mask = 1; mask < states; mask++) {
            for (int u = 0; u < n; u++) {
                if ((mask & (1 << u)) == 0) continue;
                if (dp[mask][u] == INF) continue;

                for (int v = 0; v < n; v++) {
                    if ((mask & (1 << v)) != 0) continue; // already visited

                    int    newMask = mask | (1 << v);
                    double newCost = dp[mask][u] + dist[u][v];

                    if (newCost < dp[newMask][v]) {
                        dp[newMask][v]     = newCost;
                        parent[newMask][v] = u;
                    }
                }
            }
        }

        // Find best last node before returning home
        int    fullMask = states - 1;
        double best     = INF;
        int    lastNode = -1;

        for (int u = 1; u < n; u++) {
            double total = dp[fullMask][u] + dist[u][0];
            if (total < best) { best = total; lastNode = u; }
        }

        // Reconstruct path (local indices)
        int[] localPath = new int[n + 1];
        int   mask      = fullMask;
        int   cur       = lastNode;
        for (int i = n - 1; i >= 0; i--) {
            localPath[i] = cur;
            int prev = parent[mask][cur];
            mask ^= (1 << cur);
            cur = prev;
        }
        localPath[n] = 0; // return home

        // Remap to global indices
        int[] globalPath = new int[n + 1];
        int[] nodes = new int[n];
        nodes[0] = 0;
        for (int i = 0; i < nodeSubset.length; i++) nodes[i + 1] = nodeSubset[i];

        for (int i = 0; i <= n; i++)
            globalPath[i] = nodes[localPath[i] < n ? localPath[i] : 0];

        return globalPath;
    }

    public double optimalCost(int[] nodeSubset) {
        solve(nodeSubset);
        int fullMask = (1 << n) - 1;
        double best = INF;
        for (int u = 1; u < n; u++) {
            double total = dp[fullMask][u] + dist[u][0];
            if (total < best) best = total;
        }
        return best;
    }
}
