import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.HashSet;
import java.util.Set;

/**
 * SimulationApp class
 * JavaFX entry point, makes the simulation.
 * @author Ethan Kazenske
 * @version 1.0
 * @since 2026-11-27
 */

public class SimulationApp extends Application {
    private static final double WINDOW_WIDTH = 1200;
    private static final double WINDOW_HEIGHT = 800;
    private static final double TIME_STEP = 0.1;
    private static final double MIN_TIME_WARP = 1.0;
    private static final double MAX_TIME_WARP = 8192.0;
    private static final int MAX_STEPS_PER_FRAME = 5000;
    private static final double VIEW_RADII = 4.0;
    private static final double ORBIT_ALTITUDE = 400_000;
    private static final double SHIP_MASS = 1000;
    private Simulation simulation;
    private Renderer renderer;
    private Canvas canvas;
    private double timeWarp;
    private double timeAccumulator;
    private long lastFrameTime;
    private final Set<KeyCode> heldKeys = new HashSet<>();

    @Override
    public void start(Stage stage) {
        canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle("Orbital Simulator");
        stage.setScene(scene);
        stage.setResizable(false);
        double halfView = Math.min(WINDOW_WIDTH, WINDOW_HEIGHT) / 2.0;
        double scale = halfView / (VIEW_RADII * Constants.EARTH_RADIUS);
        renderer = new Renderer(canvas, new Camera(scale, WINDOW_WIDTH, WINDOW_HEIGHT));
        simulation = createCircularOrbit();
        simulation.start();
        timeWarp = MIN_TIME_WARP;
        timeAccumulator = 0.0;
        lastFrameTime = 0;
        scene.setOnKeyPressed(this::handleKeyPressed);
        scene.setOnKeyReleased(this::handleKeyReleased);
        stage.focusedProperty().addListener((observable, wasFocused, isFocused) -> heldKeys.clear());
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateBurnMode();
                advanceSimulation(now);
                renderer.render(simulation);
                drawHud();
            }
        };
        timer.start();
        stage.show();
    }

    private Simulation createCircularOrbit() {
        CelestialBody earth = new CelestialBody("Earth", Constants.EARTH_MASS,
                new Vector2D(0, 0), new Vector2D(0, 0), Constants.EARTH_RADIUS);
        double r = Constants.EARTH_RADIUS + ORBIT_ALTITUDE;
        double speed = Math.sqrt(Constants.GRAVITATIONAL_CONSTANT * Constants.EARTH_MASS / r);
        Spacecraft ship = new Spacecraft("Ship", SHIP_MASS, new Vector2D(r, 0), new Vector2D(0, speed));
        return new Simulation(earth, ship, TIME_STEP);
    }

    private void advanceSimulation(long now) {
        if (lastFrameTime == 0) {
            lastFrameTime = now;
            return;
        }
        double realSeconds = (now - lastFrameTime) / 1_000_000_000.0;
        lastFrameTime = now;
        timeAccumulator += realSeconds * timeWarp;
        int steps = 0;
        while (timeAccumulator >= TIME_STEP && steps < MAX_STEPS_PER_FRAME) {
            simulation.update();
            timeAccumulator -= TIME_STEP;
            steps++;
        }
        if (steps == MAX_STEPS_PER_FRAME) {
            timeAccumulator = 0.0;
        }
    }

    private void updateBurnMode() {
        Spacecraft ship = simulation.getShip();
        if (heldKeys.contains(KeyCode.W)) {
            ship.setBurnMode(BurnMode.PROGRADE);
        } else if (heldKeys.contains(KeyCode.S)) {
            ship.setBurnMode(BurnMode.RETROGRADE);
        } else if (heldKeys.contains(KeyCode.D)) {
            ship.setBurnMode(BurnMode.RADIAL_OUT);
        } else if (heldKeys.contains(KeyCode.A)) {
            ship.setBurnMode(BurnMode.RADIAL_IN);
        } else {
            ship.setBurnMode(BurnMode.NONE);
        }
    }

    private void drawHud() {
        Spacecraft ship = simulation.getShip();
        CelestialBody earth = simulation.getEarth();
        double distance = ship.getPosition().distanceTo(earth.getPosition());
        double altitudeKm = (distance - earth.getRadius()) / 1000.0;
        double speed = ship.getVelocity().magnitude();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Monospaced", 16));
        gc.fillText("Time warp: " + (long) timeWarp + "x  (Up / Down to change)", 16, 28);
        gc.fillText("Elapsed:   " + formatTime(simulation.getSimulationTime()), 16, 50);
        gc.fillText(String.format("Altitude:  %.1f km", altitudeKm), 16, 72);
        gc.fillText(String.format("Speed:     %.1f m/s", speed), 16, 94);
        gc.fillText("Burn:      " + ship.getBurnMode(), 16, 116);
        gc.fillText("W/S prograde/retrograde  D/A radial out/in  Space pause", 16, 138);
        if (!simulation.isRunning()) {
            gc.fillText("PAUSED", 16, 160);
        }
    }

    private String formatTime(double totalSeconds) {
        long seconds = (long) totalSeconds;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    private void handleKeyReleased(KeyEvent event) {
        heldKeys.remove(event.getCode());
    }

    private void handleKeyPressed(KeyEvent event) {
        heldKeys.add(event.getCode());
        switch (event.getCode()) {
            case SPACE:
                if (simulation.isRunning()) {
                    simulation.pause();
                } else {
                    simulation.start();
                }
                break;
            case UP:
                timeWarp = Math.min(timeWarp * 2, MAX_TIME_WARP);
                break;
            case DOWN:
                timeWarp = Math.max(timeWarp / 2, MIN_TIME_WARP);
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
