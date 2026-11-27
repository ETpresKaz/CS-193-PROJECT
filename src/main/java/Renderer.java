import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Renderer class
 * Uses Camera with canvas to draw the sim
 * @author Ethan Kazenske
 * @version 1.0
 * @since 2026-11-27
 */
public class Renderer {
    private static final double DEFAULT_VIEW_RADII = 4.0;
    private static final double SHIP_SIZE = 10.0;
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final Color PLANET_COLOR = Color.DODGERBLUE;
    private static final Color SHIP_COLOR = Color.WHITE;
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final Camera camera;

    public Renderer(Canvas canvas) {
        if (canvas == null) {
            throw new IllegalArgumentException("Canvas cannot be null");
        }
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        double halfView = Math.min(canvas.getWidth(), canvas.getHeight()) / 2.0;
        double scale = halfView / (DEFAULT_VIEW_RADII * Constants.EARTH_RADIUS);
        this.camera = new Camera(scale, canvas.getWidth(), canvas.getHeight());
    }

    public Renderer(Canvas canvas, Camera camera) {
        if (canvas == null) {
            throw new IllegalArgumentException("Canvas cannot be null");
        }
        if (camera == null) {
            throw new IllegalArgumentException("Camera cannot be null");
        }
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.camera = camera;
    }

    public void render(Simulation simulation) {
        if (simulation == null) {
            throw new IllegalArgumentException("Simulation cannot be null");
        }
        clear();
        CelestialBody planet = simulation.getEarth();
        Spacecraft ship = simulation.getShip();
        drawBody(planet);
        drawSpacecraft(ship);
    }

    private void clear() {
        gc.setFill(BACKGROUND_COLOR);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawBody(CelestialBody body) {
        double centerX = camera.worldToScreenX(body.getPosition().getX());
        double centerY = camera.worldToScreenY(body.getPosition().getY());
        double radius = body.getRadius() * camera.getScale();
        gc.setFill(PLANET_COLOR);
        gc.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
    }

    private void drawSpacecraft(Spacecraft ship) {
        double centerX = camera.worldToScreenX(ship.getPosition().getX());
        double centerY = camera.worldToScreenY(ship.getPosition().getY());
        Vector2D velocity = ship.getVelocity();
        double heading;
        if (velocity.magnitudeSquared() == 0) {
            heading = -Math.PI / 2;
        } else {
            heading = Math.atan2(-velocity.getY(), velocity.getX());
        }
        double[][] local = {
                { SHIP_SIZE, 0 },
                { -SHIP_SIZE * 0.6, -SHIP_SIZE * 0.5 },
                { -SHIP_SIZE * 0.6, SHIP_SIZE * 0.5 }
        };
        double cos = Math.cos(heading);
        double sin = Math.sin(heading);
        double[] xPoints = new double[3];
        double[] yPoints = new double[3];
        for (int i = 0; i < 3; i++) {
            xPoints[i] = centerX + local[i][0] * cos - local[i][1] * sin;
            yPoints[i] = centerY + local[i][0] * sin + local[i][1] * cos;
        }
        gc.setFill(SHIP_COLOR);
        gc.fillPolygon(xPoints, yPoints, 3);
    }

    public Camera getCamera() {
        return camera;
    }
}
