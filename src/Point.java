/**
 * Point.java
 * Represents a location — either Home (0,0) or a relative's house.
 */
public class Point {
    public String name;
    public double x, y;

    public Point(String name, double x, double y) {
        this.name = name;
        this.x    = x;
        this.y    = y;
    }

    /** Euclidean distance to another point */
    public double distanceTo(Point other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return String.format("%s (%.1f, %.1f)", name, x, y);
    }
}
