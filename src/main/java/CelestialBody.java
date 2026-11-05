/**
 * CelestialBody class extends Body
 * Represents a Celestial Body in the simulation
 * @author Ethan Kazenske
 * @version 1.0
 * @since 2026-11-05
 */

public class CelestialBody extends Body {
    private final double radius;

    public CelestialBody(String name, double mass, Vector2D position, Vector2D velocity, double radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }
        super(name, mass, position, velocity);
        this.radius = radius;
    }

    public CelestialBody(String name, double mass, double radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }
        super(name, mass);
        this.radius = radius;
    }

    public double getRadius() {
        return this.radius;
    }

    @Override
    public String toString() {
        return "Celestial "+ super.toString() + ", radius=" + radius;
    }
}
