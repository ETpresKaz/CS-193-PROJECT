
/**
 * Body class
 * Represents a body in the simulation
 * @author Ethan Kazenske
 * @version 1.0
 * @since 2026-11-05
 */

public class Body {
    private String name;
    private double mass;
    private Vector2D position;
    private Vector2D velocity;
    private Vector2D acceleration;

    public Body(String name, double mass, Vector2D position, Vector2D velocity) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (mass <= 0) {
            throw new IllegalArgumentException("Mass must be positive");
        }
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        if (velocity == null) {
            throw new IllegalArgumentException("Velocity cannot be null");
        }
        this.name = name;
        this.mass = mass;
        this.position = position;
        this.velocity = velocity;
        this.acceleration = new Vector2D(0, 0);
    }

    public Body(String name, double mass) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (mass <= 0) {
            throw new IllegalArgumentException("Mass must be positive");
        }
        this.name = name;
        this.mass = mass;
        this.position = new Vector2D(0, 0);
        this.velocity = new Vector2D(0, 0);
        this.acceleration = new Vector2D(0, 0);
    }

    public String getName() {
        return this.name;
    }

    public double getMass() {
        return this.mass;
    }

    public Vector2D getPosition() {
        return this.position;
    }

    public Vector2D getVelocity() {
        return this.velocity;
    }

    public Vector2D getAcceleration() {
        return this.acceleration;
    }

    public void setPosition(Vector2D position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        this.position = position;
    }

    public void setVelocity(Vector2D velocity) {
        if (velocity == null) {
            throw new IllegalArgumentException("Velocity cannot be null");
        }
        this.velocity = velocity;
    }

    public void setAcceleration(Vector2D acceleration) {
        if (acceleration == null) {
            throw new IllegalArgumentException("Acceleration cannot be null");
        }
        this.acceleration = acceleration;
    }

    @Override
    public String toString() {
        return "Body name=" + name + ", mass=" + mass + ", position=" + position +
                ", velocity=" + velocity + ", acceleration=" + acceleration;
    }
}
