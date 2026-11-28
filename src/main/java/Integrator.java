/**
 * Integrator class
 * Performs numerical integration for updating body positions and velocities.
 * @author Ethan Kazenske
 * @version 1.0
 * @since 2026-11-23
 */

public class Integrator {

    public void step(Body body, Body ship, double dt) {
        step(body, ship, new Vector2D(0, 0), dt);
    }

    public void step(Body body, Body ship, Vector2D thrust, double dt) {
        if (body == null || ship == null) {
            throw new IllegalArgumentException("Body and ship cannot be null");
        }
        if (thrust == null) {
            throw new IllegalArgumentException("Thrust cannot be null");
        }
        if (dt <= 0) {
            throw new IllegalArgumentException("Time step must be positive");
        }
        Vector2D oldAcceleration = Gravity.calculateAcceleration(body, ship).add(thrust);
        // velocity verlet
        // s_new = s_old + v_old * dt + 1/2 * a_old * dt^2
        Vector2D newPosition = ship.getPosition().add(ship.getVelocity().scale(dt)).add(oldAcceleration.scale(0.5 * dt * dt));
        ship.setPosition(newPosition);
        Vector2D newAcceleration = Gravity.calculateAcceleration(body, ship).add(thrust);
        Vector2D averageAcceleration = oldAcceleration.add(newAcceleration).scale(0.5);
        Vector2D newVelocity = ship.getVelocity().add(averageAcceleration.scale(dt));
        ship.setVelocity(newVelocity);
        ship.setAcceleration(newAcceleration);
    }

}
