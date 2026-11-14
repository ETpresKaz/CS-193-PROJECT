import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GravityTest {

    private static final double EPSILON = 1e-9;

    private static final double EARTH_MASS = 5.972e24;
    private static final double EARTH_RADIUS = 6.371e6;
    private static final double SPACECRAFT_MASS = 1000.0;

    // Helper method for creating bodies
    private Body createBody(String name, double mass, double x, double y) {
        return new Body(
            name,
            mass,
            new Vector2D(x, y),
            new Vector2D(0, 0)
        );
    }

    // Test gravitational acceleration near Earth's surface
    @Test
    public void testEarthSurfaceGravity() {
        Body earth = createBody("Earth", EARTH_MASS, 0, 0);
        Body spacecraft = createBody("Spacecraft", SPACECRAFT_MASS, EARTH_RADIUS, 0);

        Vector2D acceleration = Gravity.calculateAcceleration(earth, spacecraft);

        double expected = Constants.GRAVITATIONAL_CONSTANT * EARTH_MASS / (EARTH_RADIUS * EARTH_RADIUS);

        assertEquals(-expected, acceleration.getX(), EPSILON);
        assertEquals(0.0, acceleration.getY(), EPSILON);
        assertEquals(9.82, acceleration.magnitude(), 0.02);
    }

    // Test gravity pointing in the positive X direction
    @Test
    public void testPositiveXDirection() {
        Body planet = createBody("Planet", EARTH_MASS, 1000, 0);
        Body spacecraft = createBody("Spacecraft", SPACECRAFT_MASS, 0, 0);

        Vector2D acceleration = Gravity.calculateAcceleration(planet, spacecraft);

        assertTrue(acceleration.getX() > 0);
        assertEquals(0.0, acceleration.getY(), EPSILON);
    }

    // Test gravity pointing in the negative X direction
    @Test
    public void testNegativeXDirection() {
        Body planet = createBody("Planet", EARTH_MASS, 0, 0);
        Body spacecraft = createBody("Spacecraft", SPACECRAFT_MASS, 1000, 0);

        Vector2D acceleration = Gravity.calculateAcceleration(planet, spacecraft);

        assertTrue(acceleration.getX() < 0);
        assertEquals(0.0, acceleration.getY(), EPSILON);
    }

    // Test gravity pointing in the positive Y direction
    @Test
    public void testPositiveYDirection() {
        Body planet = createBody("Planet", EARTH_MASS, 0, 1000);
        Body spacecraft = createBody("Spacecraft", SPACECRAFT_MASS, 0, 0);

        Vector2D acceleration = Gravity.calculateAcceleration(planet, spacecraft);

        assertEquals(0.0, acceleration.getX(), EPSILON);
        assertTrue(acceleration.getY() > 0);
    }

    // Test gravity pointing in the negative Y direction
    @Test
    public void testNegativeYDirection() {
        Body planet = createBody("Planet", EARTH_MASS, 0, 0);
        Body spacecraft = createBody("Spacecraft", SPACECRAFT_MASS, 0, 1000);

        Vector2D acceleration = Gravity.calculateAcceleration(planet, spacecraft);

        assertEquals(0.0, acceleration.getX(), EPSILON);
        assertTrue(acceleration.getY() < 0);
    }

    // Test gravity in both X and Y directions
    @Test
    public void testDiagonalGravity() {
        Body planet = createBody("Planet", EARTH_MASS, 0, 0);
        Body spacecraft = createBody("Spacecraft", SPACECRAFT_MASS, 1000, 1000);

        Vector2D acceleration = Gravity.calculateAcceleration(planet, spacecraft);

        assertTrue(acceleration.getX() < 0);
        assertTrue(acceleration.getY() < 0);

        // Equal displacement in X and Y should produce equal acceleration components
        assertEquals(acceleration.getX(), acceleration.getY(), EPSILON);
    }

    // Test inverse-square law: doubling distance quarters acceleration
    @Test
    public void testInverseSquareLaw() {
        Body planet = createBody("Planet", EARTH_MASS, 0, 0);

        Body spacecraft1 = createBody("Spacecraft1", SPACECRAFT_MASS, 1000, 0);
        Body spacecraft2 = createBody("Spacecraft2", SPACECRAFT_MASS, 2000, 0);

        Vector2D acceleration1 = Gravity.calculateAcceleration(planet, spacecraft1);
        Vector2D acceleration2 = Gravity.calculateAcceleration(planet, spacecraft2);

        assertEquals(
            acceleration1.magnitude() / 4.0,
            acceleration2.magnitude(),
            EPSILON
        );
    }

    // Test tripling distance reduces acceleration by factor of nine
    @Test
    public void testTripleDistance() {
        Body planet = createBody("Planet", EARTH_MASS, 0, 0);

        Body spacecraft1 = createBody("Spacecraft1", SPACECRAFT_MASS, 1000, 0);
        Body spacecraft2 = createBody("Spacecraft2", SPACECRAFT_MASS, 3000, 0);

        Vector2D acceleration1 = Gravity.calculateAcceleration(planet, spacecraft1);
        Vector2D acceleration2 = Gravity.calculateAcceleration(planet, spacecraft2);

        assertEquals(
            acceleration1.magnitude() / 9.0,
            acceleration2.magnitude(),
            EPSILON
        );
    }

    // Test doubling planet mass doubles gravitational acceleration
    @Test
    public void testDoublePlanetMass() {
        Body planet1 = createBody("Planet1", EARTH_MASS, 0, 0);
        Body planet2 = createBody("Planet2", EARTH_MASS * 2, 0, 0);

        Body spacecraft = createBody("Spacecraft", SPACECRAFT_MASS, 1000, 0);

        Vector2D acceleration1 = Gravity.calculateAcceleration(planet1, spacecraft);
        Vector2D acceleration2 = Gravity.calculateAcceleration(planet2, spacecraft);

        assertEquals(
            acceleration1.magnitude() * 2.0,
            acceleration2.magnitude(),
            EPSILON
        );
    }

    // Test spacecraft mass does not affect gravitational acceleration
    @Test
    public void testSpacecraftMassIndependence() {
        Body planet = createBody("Planet", EARTH_MASS, 0, 0);

        Body lightSpacecraft = createBody("Light", 100, 1000, 0);
        Body heavySpacecraft = createBody("Heavy", 100000, 1000, 0);

        Vector2D lightAcceleration = Gravity.calculateAcceleration(planet, lightSpacecraft);
        Vector2D heavyAcceleration = Gravity.calculateAcceleration(planet, heavySpacecraft);

        assertEquals(lightAcceleration.getX(), heavyAcceleration.getX(), EPSILON);
        assertEquals(lightAcceleration.getY(), heavyAcceleration.getY(), EPSILON);
    }

    // Test gravity works when planet is not at origin
    @Test
    public void testPlanetNotAtOrigin() {
        Body planet = createBody("Planet", EARTH_MASS, 5000, 5000);
        Body spacecraft = createBody("Spacecraft", SPACECRAFT_MASS, 6000, 5000);

        Vector2D acceleration = Gravity.calculateAcceleration(planet, spacecraft);

        assertTrue(acceleration.getX() < 0);
        assertEquals(0.0, acceleration.getY(), EPSILON);
    }

    // Test zero-mass source cannot be created (Body requires positive mass)
    @Test
    public void testZeroMassSource() {
        assertThrows(
            IllegalArgumentException.class,
            () -> createBody("Planet", 0, 0, 0)
        );
    }

    // Test null source throws exception
    @Test
    public void testNullSource() {
        Body spacecraft = createBody("Spacecraft", SPACECRAFT_MASS, 1000, 0);

        assertThrows(
            IllegalArgumentException.class,
            () -> Gravity.calculateAcceleration(null, spacecraft)
        );
    }

    // Test null target throws exception
    @Test
    public void testNullTarget() {
        Body planet = createBody("Planet", EARTH_MASS, 0, 0);

        assertThrows(
            IllegalArgumentException.class,
            () -> Gravity.calculateAcceleration(planet, null)
        );
    }

    // Test identical positions throw exception
    @Test
    public void testIdenticalPositions() {
        Body planet = createBody("Planet", EARTH_MASS, 1000, 1000);
        Body spacecraft = createBody("Spacecraft", SPACECRAFT_MASS, 1000, 1000);

        assertThrows(
            IllegalArgumentException.class,
            () -> Gravity.calculateAcceleration(planet, spacecraft)
        );
    }

    // Test calculating gravity does not modify body positions
    @Test
    public void testDoesNotModifyPositions() {
        Body planet = createBody("Planet", EARTH_MASS, 0, 0);
        Body spacecraft = createBody("Spacecraft", SPACECRAFT_MASS, 1000, 2000);

        Gravity.calculateAcceleration(planet, spacecraft);

        assertEquals(0.0, planet.getPosition().getX(), EPSILON);
        assertEquals(0.0, planet.getPosition().getY(), EPSILON);

        assertEquals(1000.0, spacecraft.getPosition().getX(), EPSILON);
        assertEquals(2000.0, spacecraft.getPosition().getY(), EPSILON);
    }
}