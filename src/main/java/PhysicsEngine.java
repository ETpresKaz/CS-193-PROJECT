/**
 * PhysicsEngine class
 * Manages the physics simulation for bodies.
 * @author Ethan Kazenske
 * @version 1.0
 * @since 2026-11-23
 */

public class PhysicsEngine {
    private final Integrator integrator;

    public PhysicsEngine() {
        this.integrator = new Integrator();
    }

    public void update(Body body, Body ship, double dt) {
        this.integrator.step(body, ship, dt);
    }

    public void update(Body body, Body ship, Vector2D thrust, double dt) {
        this.integrator.step(body, ship, thrust, dt);
    }
}
