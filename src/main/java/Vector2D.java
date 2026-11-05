/**
 * Vector2D class
 * 2D vector x,y for position, velocity, acceleration
 * @author Ethan Kazenske
 * @version 1.0
 * @since 2026-11-05
 */

public class Vector2D {
    private final double x;
    private final double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double magnitude() {
        return Math.hypot(x, y);
    }

    public double magnitudeSquared() {
        return x * x + y * y;
    }

    public Vector2D add(Vector2D other) {
        if (other == null) {
            throw new IllegalArgumentException("Argument must be a non-null instance of Vector2D");
        }
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    public Vector2D subtract(Vector2D other) {
        if (other == null) {
            throw new IllegalArgumentException("Argument must be a non-null instance of Vector2D");
        }
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    public Vector2D scale(double scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    public Vector2D normalize() {
        double mag = this.magnitude();
        if (mag == 0) {
            throw new ArithmeticException("Cannot normalize a zero vector");
        }
        return new Vector2D(this.x / mag, this.y / mag);
    }

    public double dot(Vector2D other) {
        if (other == null) {
            throw new IllegalArgumentException("Argument must be a non-null instance of Vector2D");
        }
        return this.x * other.x + this.y * other.y;
    }

    public double cross(Vector2D other) {
        if (other == null) {
            throw new IllegalArgumentException("Argument must be a non-null instance of Vector2D");
        }
        return this.x * other.y - this.y * other.x;
    }

    public double distanceTo(Vector2D other) {
        if (other == null) {
            throw new IllegalArgumentException("Argument must be a non-null instance of Vector2D");
        }
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double distanceSquaredTo(Vector2D other) {
        if (other == null) {
            throw new IllegalArgumentException("Argument must be a non-null instance of Vector2D");
        }
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return dx * dx + dy * dy;
    }

    public Vector2D perpendicular() {
        return new Vector2D(-this.y, this.x);
    }

    public Vector2D negate() {
        return new Vector2D(-this.x, -this.y);
    }

    public double angle(Vector2D other) {
    if (other == null) {
        throw new IllegalArgumentException("Vector cannot be null");
    }
    if (this.magnitudeSquared() == 0 ||
            other.magnitudeSquared() == 0) {
        throw new ArithmeticException(
                "Cannot calculate angle with a zero vector");
    }
    return Math.atan2(
            Math.abs(this.cross(other)),
            this.dot(other)
    );
}

    public Vector2D rotate(double angle) {
        double cosTheta = Math.cos(angle);
        double sinTheta = Math.sin(angle);
        double newX = this.x * cosTheta - this.y * sinTheta;
        double newY = this.x * sinTheta + this.y * cosTheta;
        return new Vector2D(newX, newY);
    }

    @Override
    public String toString() {
        return "Vector2D(" + x + ", " + y + ")";
    }
}