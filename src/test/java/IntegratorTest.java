import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntegratorTest {

    private static final double G = Constants.GRAVITATIONAL_CONSTANT;
    private static final double EARTH_MASS = Constants.EARTH_MASS;
    private static final double ORBIT_RADIUS = 6_771_000.0; // ~400 km altitude (LEO)

    private final Integrator integrator = new Integrator();

    @Test
    public void nullBodyTest() {
        Body ship = createShip();
        assertThrows(IllegalArgumentException.class, () -> integrator.step(null, ship, 1.0));
    }

    @Test
    public void nullShipTest() {
        Body earth = createEarth();
        assertThrows(IllegalArgumentException.class, () -> integrator.step(earth, null, 1.0));
    }

    @Test
    public void zeroTimeStepTest() {
        Body earth = createEarth();
        Body ship = createShip();
        assertThrows(IllegalArgumentException.class, () -> integrator.step(earth, ship, 0.0));
    }

    @Test
    public void negativeTimeStepTest() {
        Body earth = createEarth();
        Body ship = createShip();
        assertThrows(IllegalArgumentException.class, () -> integrator.step(earth, ship, -1.0));
    }

    @Test
    public void positionUpdateTest() {
        Body earth = createEarth();
        Body ship = createShip();
        Vector2D oldPosition = ship.getPosition();
        Vector2D oldVelocity = ship.getVelocity();
        Vector2D acceleration = Gravity.calculateAcceleration(earth, ship);
        double dt = 1.0;
        Vector2D expectedPosition = oldPosition.add(oldVelocity.scale(dt)).add(acceleration.scale(0.5 * dt * dt));
        integrator.step(earth, ship, dt);
        assertEquals(expectedPosition.getX(),ship.getPosition().getX(), 1e-6);
        assertEquals(expectedPosition.getY(),ship.getPosition().getY(), 1e-6);
    }

    @Test
    public void velocityUpdateTest() {
        Body earth = createEarth();
        Body ship = createShip();
        Vector2D oldVelocity = ship.getVelocity();
        double dt = 1.0;
        Vector2D oldAcceleration = Gravity.calculateAcceleration(earth, ship);
        Vector2D expectedPosition = ship.getPosition().add(oldVelocity.scale(dt)).add(oldAcceleration.scale(0.5 * dt * dt));
        Body predictedShip = new Body("PredictedShip", ship.getMass(), expectedPosition, oldVelocity);
        Vector2D newAcceleration = Gravity.calculateAcceleration(earth, predictedShip);
        Vector2D expectedVelocity = oldVelocity.add(oldAcceleration.add(newAcceleration).scale(0.5 * dt));
        integrator.step(earth, ship, dt);
        assertEquals(expectedVelocity.getX(), ship.getVelocity().getX(), 1e-6);
        assertEquals(expectedVelocity.getY(), ship.getVelocity().getY(), 1e-6);
    }

    @Test
    public void accelerationUpdateTest() {
        Body earth = createEarth();
        Body ship = createShip();
        integrator.step(earth, ship, 1.0);
        // stored acceleration should match gravity at the ship's NEW position
        Vector2D expectedAcceleration = Gravity.calculateAcceleration(earth, ship);
        assertEquals(expectedAcceleration.getX(), ship.getAcceleration().getX(), 1e-9);
        assertEquals(expectedAcceleration.getY(), ship.getAcceleration().getY(), 1e-9);
    }

    @Test
    public void circularOrbitStabilityTest() {
        Body earth = createEarth();
        Body ship = createShip();
        double initialRadius = ORBIT_RADIUS;
        double dt = 1.0;
        // one full orbital period: T = 2*pi*sqrt(r^3 / GM)
        double period = 2 * Math.PI * Math.sqrt(Math.pow(ORBIT_RADIUS, 3) / (G * EARTH_MASS));
        int steps = (int) Math.round(period / dt);
        for (int i = 0; i < steps; i++) {
            integrator.step(earth, ship, dt);
        }
        double finalRadius = ship.getPosition().subtract(earth.getPosition()).magnitude();
        assertEquals(initialRadius, finalRadius, 1000.0);
    }

    @Test
    public void orbitalEnergyConservationTest() {
        Body earth = createEarth();
        Body ship = createShip();
        double initialEnergy = calculateSpecificEnergy(earth, ship);
        double dt = 1.0;
        for (int i = 0; i < 10_000; i++) {
            integrator.step(earth, ship, dt);
        }
        double finalEnergy = calculateSpecificEnergy(earth, ship);
        double relativeError = Math.abs((finalEnergy - initialEnergy) / initialEnergy);
        assertTrue(relativeError < 1e-5, "Orbital energy drifted too much: " + relativeError);
    }

    @Test
    public void sourceBodyUnchangedTest() {
        Body earth = createEarth();
        Body ship = createShip();
        integrator.step(earth, ship, 1.0);
        assertEquals(0.0, earth.getPosition().getX());
        assertEquals(0.0, earth.getPosition().getY());
        assertEquals(0.0, earth.getVelocity().getX());
        assertEquals(0.0, earth.getVelocity().getY());
    }

    private Body createEarth() {
        return new Body("Earth", EARTH_MASS, new Vector2D(0, 0), new Vector2D(0, 0));
    }

    private Body createShip() {
        double orbitalVelocity = Math.sqrt(G * EARTH_MASS / ORBIT_RADIUS);
        return new Body("Ship", 1000, new Vector2D(ORBIT_RADIUS, 0), new Vector2D(0, orbitalVelocity));
    }
    
    private double calculateSpecificEnergy(Body earth, Body ship) {
        double radius = ship.getPosition().subtract(earth.getPosition()).magnitude();
        double velocitySquared = ship.getVelocity().magnitudeSquared();
        return 0.5 * velocitySquared - (G * earth.getMass() / radius);
    }
}