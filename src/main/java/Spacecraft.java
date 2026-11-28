/**
 * Spacecraft class extends Body
 * Represents a spacecraft in the simulation
 * @author Ethan Kazenske
 * @version 1.0
 * @since 2026-11-27
 */

public class Spacecraft extends Body {
    public static final double THRUST_ACCELERATION = 10.0;
    private BurnMode burnMode = BurnMode.NONE;

    public Spacecraft(String name, double mass, Vector2D position, Vector2D velocity) {
        super(name, mass, position, velocity);
    }

    public Spacecraft(String name, double mass) {
        super(name, mass);
    }

    public BurnMode getBurnMode() {
        return burnMode;
    }

    public void setBurnMode(BurnMode burnMode) {
        if (burnMode == null) {
            throw new IllegalArgumentException("Burn mode cannot be null");
        }
        this.burnMode = burnMode;
    }

    public Vector2D getThrustAcceleration(Body planet) {
        if (planet == null) {
            throw new IllegalArgumentException("Planet cannot be null");
        }
        if (burnMode == BurnMode.NONE) {
            return new Vector2D(0, 0);
        }
        Vector2D direction;
        if (burnMode == BurnMode.PROGRADE || burnMode == BurnMode.RETROGRADE) {
            if (getVelocity().magnitude() == 0) {
                return new Vector2D(0, 0);
            }
            direction = getVelocity().normalize();
        } else {
            direction = getPosition().subtract(planet.getPosition()).normalize();
        }
        if (burnMode == BurnMode.RETROGRADE || burnMode == BurnMode.RADIAL_IN) {
            direction = direction.negate();
        }

        return direction.scale(THRUST_ACCELERATION);
    }

    @Override
    public String toString() {
        return "Spacecraft " + super.toString() + ", burnMode=" + burnMode;
    }
}
