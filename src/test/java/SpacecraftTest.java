import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * SpacecraftTest
 * JUnit 5 tests for Spacecraft thrust directions
 * @author Ethan Kazenske
 * @version 1.0
 * @since 2026-11-27
 */
public class SpacecraftTest {

    private static final double THRUST = Spacecraft.THRUST_ACCELERATION;

    @Test
    public void defaultBurnModeIsNoneTest() {
        Spacecraft ship = createShip();
        assertEquals(BurnMode.NONE, ship.getBurnMode());
    }

    @Test
    public void noBurnGivesZeroThrustTest() {
        Spacecraft ship = createShip();
        Vector2D thrust = ship.getThrustAcceleration(createEarth());
        assertEquals(0.0, thrust.getX());
        assertEquals(0.0, thrust.getY());
    }

    @Test
    public void progradePointsAlongVelocityTest() {
        // ship is moving straight up (+y)
        Spacecraft ship = createShip();
        ship.setBurnMode(BurnMode.PROGRADE);
        Vector2D thrust = ship.getThrustAcceleration(createEarth());
        assertEquals(0.0, thrust.getX(), 1e-9);
        assertEquals(THRUST, thrust.getY(), 1e-9);
    }

    @Test
    public void retrogradePointsAgainstVelocityTest() {
        Spacecraft ship = createShip();
        ship.setBurnMode(BurnMode.RETROGRADE);
        Vector2D thrust = ship.getThrustAcceleration(createEarth());
        assertEquals(0.0, thrust.getX(), 1e-9);
        assertEquals(-THRUST, thrust.getY(), 1e-9);
    }

    @Test
    public void radialOutPointsAwayFromPlanetTest() {
        // ship is to the right of the planet (+x)
        Spacecraft ship = createShip();
        ship.setBurnMode(BurnMode.RADIAL_OUT);
        Vector2D thrust = ship.getThrustAcceleration(createEarth());
        assertEquals(THRUST, thrust.getX(), 1e-9);
        assertEquals(0.0, thrust.getY(), 1e-9);
    }

    @Test
    public void radialInPointsTowardPlanetTest() {
        Spacecraft ship = createShip();
        ship.setBurnMode(BurnMode.RADIAL_IN);
        Vector2D thrust = ship.getThrustAcceleration(createEarth());
        assertEquals(-THRUST, thrust.getX(), 1e-9);
        assertEquals(0.0, thrust.getY(), 1e-9);
    }

    @Test
    public void progradeWithZeroVelocityGivesZeroThrustTest() {
        Spacecraft ship = new Spacecraft("Ship", 1000, new Vector2D(7_000_000, 0), new Vector2D(0, 0));
        ship.setBurnMode(BurnMode.PROGRADE);
        Vector2D thrust = ship.getThrustAcceleration(createEarth());
        assertEquals(0.0, thrust.getX());
        assertEquals(0.0, thrust.getY());
    }

    @Test
    public void nullBurnModeTest() {
        Spacecraft ship = createShip();
        assertThrows(IllegalArgumentException.class, () -> ship.setBurnMode(null));
    }

    @Test
    public void nullPlanetTest() {
        Spacecraft ship = createShip();
        assertThrows(IllegalArgumentException.class, () -> ship.getThrustAcceleration(null));
    }

    @Test
    public void progradeBurnSpeedsUpShipTest() {
        Body earth = createEarth();
        Spacecraft ship = createShip();
        double startSpeed = ship.getVelocity().magnitude();
        Integrator integrator = new Integrator();
        ship.setBurnMode(BurnMode.PROGRADE);
        for (int i = 0; i < 100; i++) {
            integrator.step(earth, ship, ship.getThrustAcceleration(earth), 0.1);
        }
        assertTrue(ship.getVelocity().magnitude() > startSpeed);
    }

    @Test
    public void retrogradeBurnSlowsDownShipTest() {
        Body earth = createEarth();
        Spacecraft ship = createShip();
        double startSpeed = ship.getVelocity().magnitude();
        Integrator integrator = new Integrator();
        ship.setBurnMode(BurnMode.RETROGRADE);
        for (int i = 0; i < 100; i++) {
            integrator.step(earth, ship, ship.getThrustAcceleration(earth), 0.1);
        }
        assertTrue(ship.getVelocity().magnitude() < startSpeed);
    }

    private Body createEarth() {
        return new Body("Earth", Constants.EARTH_MASS, new Vector2D(0, 0), new Vector2D(0, 0));
    }

    private Spacecraft createShip() {
        double radius = 6_771_000.0;
        double speed = Math.sqrt(Constants.GRAVITATIONAL_CONSTANT * Constants.EARTH_MASS / radius);
        return new Spacecraft("Ship", 1000, new Vector2D(radius, 0), new Vector2D(0, speed));
    }
}
