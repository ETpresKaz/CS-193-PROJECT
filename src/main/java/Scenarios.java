/**
 * Scenarios class
 * Builds the starting setups the user can load with keys 1, 2, 3.
 * @author Ethan Kazenske
 * @version 1.0
 * @since 2026-11-27
 */

public final class Scenarios {
    private static final double SHIP_MASS = 1000;

    private Scenarios() {
        // Prevent instantiation
    }

    public static Simulation lowOrbit(double timeStep) {
        double r = Constants.EARTH_RADIUS + 400_000;
        return build(r, circularSpeed(r), timeStep);
    }

    public static Simulation ellipticalOrbit(double timeStep) {
        double r = Constants.EARTH_RADIUS + 400_000;
        return build(r, circularSpeed(r) * 1.15, timeStep);
    }

    public static Simulation highOrbit(double timeStep) {
        double r = Constants.EARTH_RADIUS + 10_000_000;
        return build(r, circularSpeed(r), timeStep);
    }

    private static double circularSpeed(double r) {
        return Math.sqrt(Constants.GRAVITATIONAL_CONSTANT * Constants.EARTH_MASS / r);
    }

    private static Simulation build(double r, double speed, double timeStep) {
        CelestialBody earth = new CelestialBody("Earth", Constants.EARTH_MASS, new Vector2D(0, 0), new Vector2D(0, 0), Constants.EARTH_RADIUS);
        Spacecraft ship = new Spacecraft("Ship", SHIP_MASS, new Vector2D(r, 0), new Vector2D(0, speed));
        return new Simulation(earth, ship, timeStep);
    }
}
