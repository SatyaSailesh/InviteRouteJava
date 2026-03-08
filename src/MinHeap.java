/**
 * MinHeap.java
 * ─────────────────────────────────────────────────────────────
 * DSA: Hand-coded Min-Heap (Priority Queue)
 *
 * Operations:
 *   push  → O(log n)   bubble-up
 *   pop   → O(log n)   sink-down
 *   peek  → O(1)
 *
 * Used by:
 *   • Dijkstra   — always expand shortest-known node
 *   • Greedy     — always pick nearest unvisited relative
 * ─────────────────────────────────────────────────────────────
 */
public class MinHeap {

    /** A single entry in the heap */
    public static class Entry implements Comparable<Entry> {
        public int    node;       // index into allPoints[]
        public double priority;   // distance / cost

        public Entry(int node, double priority) {
            this.node     = node;
            this.priority = priority;
        }

        @Override
        public int compareTo(Entry other) {
            return Double.compare(this.priority, other.priority);
        }
    }

    private Entry[] heap;
    private int     size;

    public MinHeap(int capacity) {
        heap = new Entry[capacity + 1];
        size = 0;
    }

    public boolean isEmpty() { return size == 0; }
    public int     size()    { return size; }

    public void push(int node, double priority) {
        heap[++size] = new Entry(node, priority);
        bubbleUp(size);
    }

    public Entry pop() {
        if (isEmpty()) return null;
        Entry top = heap[1];
        heap[1] = heap[size--];
        sinkDown(1);
        return top;
    }

    public Entry peek() {
        return isEmpty() ? null : heap[1];
    }

    // ── Internal helpers ──────────────────────────────────────

    private void bubbleUp(int i) {
        while (i > 1 && heap[i].priority < heap[parent(i)].priority) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    private void sinkDown(int i) {
        while (true) {
            int smallest = i;
            int l = left(i), r = right(i);
            if (l <= size && heap[l].priority < heap[smallest].priority) smallest = l;
            if (r <= size && heap[r].priority < heap[smallest].priority) smallest = r;
            if (smallest == i) break;
            swap(i, smallest);
            i = smallest;
        }
    }

    private int parent(int i) { return i / 2; }
    private int left(int i)   { return 2 * i; }
    private int right(int i)  { return 2 * i + 1; }

    private void swap(int a, int b) {
        Entry tmp = heap[a];
        heap[a]   = heap[b];
        heap[b]   = tmp;
    }
}
