import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * PhysicsEngineTest
 * JUnit 5 tests for PhysicsEngine
 * @author Ethan Kazenske
 * @version 1.0
 * @since 2026-11-23
 */
public class PhysicsEngineTest {

    private static final double G = Constants.GRAVITATIONAL_CONSTANT;
    private static final double EARTH_MASS = Constants.EARTH_MASS;
    private static final double ORBIT_RADIUS = 6_771_000.0;

    private PhysicsEngine engine;

    @BeforeEach
    public void setUp() {
        engine = new PhysicsEngine();
    }

    @Test
    public void nullBodyTest() {
        Body ship = createShip();
        assertThrows(IllegalArgumentException.class, () -> engine.update(null, ship, 1.0));
    }

    @Test
    public void nullShipTest() {
        Body earth = createEarth();
        assertThrows(IllegalArgumentException.class, () -> engine.update(earth, null, 1.0));
    }

    @Test
    public void zeroTimeStepTest() {
        assertThrows(IllegalArgumentException.class, () -> engine.update(createEarth(), createShip(), 0.0));
    }

    @Test
    public void negativeTimeStepTest() {
        assertThrows(IllegalArgumentException.class, () -> engine.update(createEarth(), createShip(), -1.0));
    }

    @Test
    public void updateMatchesIntegratorStepTest() {
        Body earth = createEarth();
        Body engineShip = createShip();
        Body integratorShip = createShip();

        engine.update(earth, engineShip, 1.0);
        new Integrator().step(earth, integratorShip, 1.0);

        assertEquals(integratorShip.getPosition().getX(), engineShip.getPosition().getX(), 1e-9);
        assertEquals(integratorShip.getPosition().getY(), engineShip.getPosition().getY(), 1e-9);
        assertEquals(integratorShip.getVelocity().getX(), engineShip.getVelocity().getX(), 1e-9);
        assertEquals(integratorShip.getVelocity().getY(), engineShip.getVelocity().getY(), 1e-9);
    }

    @Test
    public void updateMovesShipTest() {
        Body earth = createEarth();
        Body ship = createShip();
        Vector2D before = ship.getPosition();
        engine.update(earth, ship, 1.0);
        assertTrue(ship.getPosition().distanceTo(before) > 0, "Ship should have moved");
    }

    @Test
    public void shipFallsTowardEarthTest() {
        // ship at rest above Earth should accelerate straight toward it
        Body earth = createEarth();
        Body ship = new Body("Ship", 1000, new Vector2D(ORBIT_RADIUS, 0), new Vector2D(0, 0));
        engine.update(earth, ship, 1.0);
        assertTrue(ship.getPosition().getX() < ORBIT_RADIUS);
        assertTrue(ship.getVelocity().getX() < 0);
        assertEquals(0.0, ship.getPosition().getY(), 1e-9);
    }

    @Test
    public void earthUnchangedTest() {
        Body earth = createEarth();
        Body ship = createShip();
        engine.update(earth, ship, 1.0);
        assertEquals(0.0, earth.getPosition().magnitude());
        assertEquals(0.0, earth.getVelocity().magnitude());
    }

    @Test
    public void repeatedUpdatesKeepCircularOrbitTest() {
        Body earth = createEarth();
        Body ship = createShip();
        for (int i = 0; i < 1000; i++) {
            engine.update(earth, ship, 1.0);
        }
        double radius = ship.getPosition().subtract(earth.getPosition()).magnitude();
        assertEquals(ORBIT_RADIUS, radius, 1000.0);
    }

    private Body createEarth() {
        return new Body("Earth", EARTH_MASS, new Vector2D(0, 0), new Vector2D(0, 0));
    }

    private Body createShip() {
        double orbitalVelocity = Math.sqrt(G * EARTH_MASS / ORBIT_RADIUS);
        return new Body("Ship", 1000, new Vector2D(ORBIT_RADIUS, 0), new Vector2D(0, orbitalVelocity));
    }
}
