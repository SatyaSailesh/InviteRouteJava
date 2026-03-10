public class Point {
    public String name;
    public double x, y;
    public Point(String name, double x, double y) {
        this.name = name;
        this.x    = x;
        this.y    = y;
    }
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
