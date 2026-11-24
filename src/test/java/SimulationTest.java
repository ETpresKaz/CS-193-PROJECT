import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * SimulationTest
 * JUnit 5 tests for Simulation
 * @author Ethan Kazenske
 * @version 1.0
 * @since 2026-11-23
 */
public class SimulationTest {

    private static final double G = Constants.GRAVITATIONAL_CONSTANT;
    private static final double ORBIT_RADIUS = 6_771_000.0;
    // deliberately not 1.0 so the tests prove the time step passed in is actually used
    private static final double TIME_STEP = 10.0;

    private CelestialBody earth;
    private Spacecraft ship;
    private Simulation simulation;

    @BeforeEach
    public void setUp() {
        earth = new CelestialBody("Earth", Constants.EARTH_MASS, new Vector2D(0, 0), new Vector2D(0, 0),
                Constants.EARTH_RADIUS);
        double orbitalVelocity = Math.sqrt(G * Constants.EARTH_MASS / ORBIT_RADIUS);
        ship = new Spacecraft("Ship", 1000, new Vector2D(ORBIT_RADIUS, 0), new Vector2D(0, orbitalVelocity));
        simulation = new Simulation(earth, ship, TIME_STEP);
    }

    @Test
    public void constructorInitialStateTest() {
        assertFalse(simulation.isRunning());
        assertEquals(0.0, simulation.getSimulationTime());
        assertSame(earth, simulation.getEarth());
        assertSame(ship, simulation.getShip());
    }

    @Test
    public void nullEarthTest() {
        assertThrows(IllegalArgumentException.class, () -> new Simulation(null, ship, TIME_STEP));
    }

    @Test
    public void nullShipTest() {
        assertThrows(IllegalArgumentException.class, () -> new Simulation(earth, null, TIME_STEP));
    }

    @Test
    public void zeroTimeStepTest() {
        assertThrows(IllegalArgumentException.class, () -> new Simulation(earth, ship, 0.0));
    }

    @Test
    public void negativeTimeStepTest() {
        assertThrows(IllegalArgumentException.class, () -> new Simulation(earth, ship, -1.0));
    }

    @Test
    public void startTest() {
        simulation.start();
        assertTrue(simulation.isRunning());
    }

    @Test
    public void pauseTest() {
        simulation.start();
        simulation.pause();
        assertFalse(simulation.isRunning());
    }

    @Test
    public void startTwiceStaysRunningTest() {
        simulation.start();
        simulation.start();
        assertTrue(simulation.isRunning());
    }

    @Test
    public void updateWhilePausedDoesNothingTest() {
        Vector2D position = ship.getPosition();
        Vector2D velocity = ship.getVelocity();
        simulation.update();
        assertEquals(0.0, simulation.getSimulationTime());
        assertSame(position, ship.getPosition());
        assertSame(velocity, ship.getVelocity());
    }

    @Test
    public void updateWhileRunningMovesShipTest() {
        Vector2D before = ship.getPosition();
        simulation.start();
        simulation.update();
        assertTrue(ship.getPosition().distanceTo(before) > 0, "Ship should have moved");
    }

    @Test
    public void updateAdvancesTimeTest() {
        simulation.start();
        simulation.update();
        assertEquals(TIME_STEP, simulation.getSimulationTime(), 1e-12);
        simulation.update();
        assertEquals(2 * TIME_STEP, simulation.getSimulationTime(), 1e-12);
    }

    @Test
    public void pauseStopsTimeTest() {
        simulation.start();
        for (int i = 0; i < 5; i++) {
            simulation.update();
        }
        simulation.pause();
        Vector2D pausedPosition = ship.getPosition();
        for (int i = 0; i < 5; i++) {
            simulation.update();
        }
        assertEquals(5 * TIME_STEP, simulation.getSimulationTime(), 1e-12);
        assertSame(pausedPosition, ship.getPosition());
    }

    @Test
    public void resumeAfterPauseTest() {
        simulation.start();
        simulation.update();
        simulation.pause();
        simulation.update();
        simulation.start();
        simulation.update();
        assertEquals(2 * TIME_STEP, simulation.getSimulationTime(), 1e-12);
    }

    @Test
    public void updateMatchesPhysicsEngineTest() {
        // a separate ship advanced directly by the engine should end up in the same place
        Spacecraft reference = new Spacecraft("Reference", ship.getMass(), ship.getPosition(), ship.getVelocity());
        PhysicsEngine engine = new PhysicsEngine();

        simulation.start();
        for (int i = 0; i < 10; i++) {
            simulation.update();
            engine.update(earth, reference, TIME_STEP);
        }

        assertEquals(reference.getPosition().getX(), ship.getPosition().getX(), 1e-9);
        assertEquals(reference.getPosition().getY(), ship.getPosition().getY(), 1e-9);
    }

    @Test
    public void earthStaysFixedTest() {
        simulation.start();
        for (int i = 0; i < 100; i++) {
            simulation.update();
        }
        assertEquals(0.0, earth.getPosition().magnitude());
    }

    @Test
    public void shipStaysInOrbitTest() {
        simulation.start();
        // ~2000 s of simulated time
        int steps = (int) (2000 / TIME_STEP);
        for (int i = 0; i < steps; i++) {
            simulation.update();
        }
        double radius = ship.getPosition().subtract(earth.getPosition()).magnitude();
        assertEquals(ORBIT_RADIUS, radius, 1000.0);
        assertTrue(radius > earth.getRadius(), "Ship should not crash into Earth");
    }
}
