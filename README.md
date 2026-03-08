# InviteRoute — Java DSA Capstone

Menu-driven Java console application for the multi-day invitation delivery routing problem.

## File Structure

```
InviteRouteJava/
├── src/
│   ├── Main.java          ← Entry point, menu loop
│   ├── Point.java         ← Location data class
│   ├── MinHeap.java       ← Hand-coded Min-Heap (Priority Queue)
│   ├── Graph.java         ← Adjacency Matrix + Adjacency List
│   ├── Dijkstra.java      ← Dijkstra's Shortest Path O((V+E)logV)
│   ├── DPTsp.java         ← Held-Karp DP Bitmask TSP O(n²·2ⁿ)
│   ├── Greedy.java        ← DistanceFunction interface
│   ├── Scheduler.java     ← Multi-day scheduling engine
│   ├── DayPlan.java       ← Day result object
│   ├── TimeUtil.java      ← Time formatting helpers
│   └── Printer.java       ← All console output / formatting
├── compile_and_run.sh     ← Linux/Mac one-click script
├── compile_and_run.bat    ← Windows one-click script
└── README.md
```

## How to Run

### Requirement
Java JDK 11 or higher — download free from https://adoptium.net/

### Linux / Mac
```bash
chmod +x compile_and_run.sh
./compile_and_run.sh
```

### Windows
Double-click `compile_and_run.bat`

### Manual (any OS)
```bash
mkdir bin
javac -d bin src/*.java
java -cp bin Main
```

## Menu Options

```
1. Enter relatives manually
2. Load sample data (8 relatives)
3. View current relatives list
4. Run Greedy Algorithm        [O(n² log n)]
5. Run Dijkstra Algorithm      [O((V+E) log V)]
6. Run DP Bitmask TSP          [O(n² · 2ⁿ), n≤15]
7. Run ALL algorithms & Compare
8. Show Distance Matrix
9. Show Adjacency List (Graph)
0. Exit
```

## DSA Concepts

| Class | DSA Concept | Complexity |
|-------|------------|------------|
| `MinHeap.java` | Min-Heap / Priority Queue | push/pop O(log n) |
| `Graph.java` | Adjacency Matrix + Adjacency List | O(n²) build |
| `Dijkstra.java` | Single-source shortest path | O((V+E) log V) |
| `DPTsp.java` | Held-Karp bitmask DP | O(n² · 2ⁿ) |
| `Scheduler.java` | Greedy nearest-neighbor | O(n² log n) |

## Problem Constraints

| Parameter | Value |
|-----------|-------|
| Start | 8:00 AM from (0, 0) |
| End | 10:00 PM (840 min/day) |
| Speed | 40 units/hour |
| Visit time | 15 min/relative |
| Distance | Euclidean |
| DP TSP limit | n ≤ 15 |
