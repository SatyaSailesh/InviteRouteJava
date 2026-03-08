/**
 * Greedy.java
 * ─────────────────────────────────────────────────────────────
 * DSA: Greedy Nearest-Neighbor TSP Heuristic
 *
 * Data Structures:
 *   • Min-Heap      — always picks nearest unvisited relative
 *   • boolean[]     — O(1) visited check
 *
 * Complexity:  Time O(n² log n) | Space O(n)
 * NOT guaranteed optimal, but very fast for large n.
 * ─────────────────────────────────────────────────────────────
 */
public class Greedy {

    /** Functional interface — pluggable distance source */
    public interface DistanceFunction {
        double distance(int i, int j);
    }
}
