/**
 * Spacecraft class extends Body
 * Represents a spacecraft in the simulation
 * @author Ethan Kazenske
 * @version 1.0
 * @since 2026-11-05
 */

public class Spacecraft extends Body {
    

    public Spacecraft(String name, double mass, Vector2D position, Vector2D velocity) {
        super(name, mass, position, velocity);
    }

    public Spacecraft(String name, double mass) {
        super(name, mass);
    }

    @Override
    public String toString() {
        return "Spacecraft " + super.toString();
    }
}
