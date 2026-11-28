import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ScenariosTest
 * JUnit 5 tests for Scenarios
 * @author Ethan Kazenske
 * @version 1.0
 * @since 2026-11-27
 */
public class ScenariosTest {

    private static final double TIME_STEP = 0.1;

    @Test
    public void scenariosStartAtTimeZeroTest() {
        assertEquals(0.0, Scenarios.lowOrbit(TIME_STEP).getSimulationTime());
        assertEquals(0.0, Scenarios.ellipticalOrbit(TIME_STEP).getSimulationTime());
        assertEquals(0.0, Scenarios.highOrbit(TIME_STEP).getSimulationTime());
    }

    @Test
    public void scenariosAreNotNullTest() {
        assertNotNull(Scenarios.lowOrbit(TIME_STEP));
        assertNotNull(Scenarios.ellipticalOrbit(TIME_STEP));
        assertNotNull(Scenarios.highOrbit(TIME_STEP));
    }

    @Test
    public void lowOrbitAltitudeTest() {
        Simulation simulation = Scenarios.lowOrbit(TIME_STEP);
        assertEquals(400_000, altitude(simulation), 1e-6);
    }

    @Test
    public void lowOrbitIsCircularSpeedTest() {
        Simulation simulation = Scenarios.lowOrbit(TIME_STEP);
        double r = Constants.EARTH_RADIUS + 400_000;
        double expected = Math.sqrt(Constants.GRAVITATIONAL_CONSTANT * Constants.EARTH_MASS / r);
        assertEquals(expected, simulation.getShip().getVelocity().magnitude(), 1e-6);
    }

    @Test
    public void ellipticalIsFasterThanLowOrbitTest() {
        double lowSpeed = Scenarios.lowOrbit(TIME_STEP).getShip().getVelocity().magnitude();
        double ellipticalSpeed = Scenarios.ellipticalOrbit(TIME_STEP).getShip().getVelocity().magnitude();
        assertTrue(ellipticalSpeed > lowSpeed);
    }

    @Test
    public void highOrbitAltitudeTest() {
        Simulation simulation = Scenarios.highOrbit(TIME_STEP);
        assertEquals(10_000_000, altitude(simulation), 1e-6);
    }

    @Test
    public void scenariosAreIndependentTest() {
        Simulation first = Scenarios.lowOrbit(TIME_STEP);
        Simulation second = Scenarios.lowOrbit(TIME_STEP);
        assertNotSame(first.getShip(), second.getShip());
        first.start();
        first.update();
        assertNotEquals(first.getShip().getPosition().getY(), second.getShip().getPosition().getY());
    }

    private double altitude(Simulation simulation) {
        double distance = simulation.getShip().getPosition().distanceTo(simulation.getEarth().getPosition());
        return distance - Constants.EARTH_RADIUS;
    }
}
