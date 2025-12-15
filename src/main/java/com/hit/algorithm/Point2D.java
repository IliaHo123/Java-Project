package com.hit.algorithm;

import java.util.Objects;

/**
 * Represents a 2D point with X and Y coordinates.
 * Used for geographic clustering (ride sharing locations, etc.)
 */
public class Point2D implements IClusterable<Point2D> {

    private final double x;
    private final double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates distance to another point using Euclidean formula.
     */
    @Override
    public double distanceTo(Point2D other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot calculate distance to null");
        }
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point2D other = (Point2D) obj;
        return Double.compare(this.x, other.x) == 0 
            && Double.compare(this.y, other.y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", x, y);
    }
}
