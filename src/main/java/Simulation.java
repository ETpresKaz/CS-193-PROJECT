/**
 * Simulation class
 * Manages the overall simulation logic.
 * @author Ethan Kazenske
 * @version 1.0
 * @since 2026-11-23
 */

public class Simulation {
    private CelestialBody earth;
    private Spacecraft ship;
    private PhysicsEngine physicsEngine;
    private boolean running;
    private double simulationTime;
    private double timeStep;

    public Simulation(CelestialBody earth, Spacecraft ship, double timeStep) {
        if (earth == null || ship == null) {
            throw new IllegalArgumentException("Body cannot be null");
        }
        if (timeStep <= 0) {
            throw new IllegalArgumentException("Time step must be positive");
        }
        this.earth = earth;
        this.ship = ship;
        this.timeStep = timeStep;
        this.physicsEngine = new PhysicsEngine();
        this.running = false;
        this.simulationTime = 0.0;
    }

    public void update() {
        if (!running) {
            return;
        }
        physicsEngine.update(earth, ship, timeStep);
        simulationTime += timeStep;
    }

    public void start() {
        running = true;
    }

    public void pause() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public double getSimulationTime() {
        return simulationTime;
    }

    public CelestialBody getEarth() {
        return earth;
    }

    public Spacecraft getShip() {
        return ship;
    }
}
