/**
 * Integrator class
 * Performs numerical integration for updating body positions and velocities.
 * @author Ethan Kazenske
 * @version 1.0
 * @since 2026-11-23
 */

public class Integrator {

    public void step(Body body, Body ship, double dt) {
        if (body == null || ship == null) {
            throw new IllegalArgumentException("Body and ship cannot be null");
        }
        if (dt <= 0) {
            throw new IllegalArgumentException("Time step must be positive");
        }
        Vector2D oldAcceleration = Gravity.calculateAcceleration(body, ship);
        // velocity verlet
        // s_new = s_old + v_old * dt + 1/2 * a_old * dt^2
        Vector2D newPosition = ship.getPosition().add(ship.getVelocity().scale(dt)).add(oldAcceleration.scale(0.5 * dt * dt));
        ship.setPosition(newPosition);
        Vector2D newAcceleration = Gravity.calculateAcceleration(body, ship);
        Vector2D averageAcceleration = oldAcceleration.add(newAcceleration).scale(0.5);
        Vector2D newVelocity = ship.getVelocity().add(averageAcceleration.scale(dt));
        ship.setVelocity(newVelocity);
        ship.setAcceleration(newAcceleration);
    }

}
