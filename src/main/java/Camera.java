/**
 * Camera class
 * Converts distances to pixels and also sizes
 * @author Ethan Kazenske
 * @version 1.0
 * @since 2026-11-27
 */

public class Camera {
    private final double scale;
    private final double viewportWidth;
    private final double viewportHeight;

    public Camera(double scale, double viewportWidth, double viewportHeight) {
        if (scale <= 0) {
            throw new IllegalArgumentException("Scale must be positive");
        }
        if (viewportWidth <= 0 || viewportHeight <= 0) {
            throw new IllegalArgumentException("Viewport size must be positive");
        }
        this.scale = scale;
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
    }

    public double worldToScreenX(double worldX) {
        return this.viewportWidth / 2.0 + worldX * this.scale;
    }

    public double worldToScreenY(double worldY) {
        return this.viewportHeight / 2.0 - worldY * this.scale;
    }

    public double getScale() {
        return this.scale;
    }

    public double getViewportWidth() {
        return this.viewportWidth;
    }

    public double getViewportHeight() {
        return this.viewportHeight;
    }
}
