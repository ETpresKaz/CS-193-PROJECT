public class Gravity {

    private Gravity() {
        // Prevent instantiation
    }

    public static Vector2D calculateAcceleration(Body source, Body target) {
        if (source == null) {
            throw new IllegalArgumentException("Source body cannot be null");
        }
        if (target == null) {
            throw new IllegalArgumentException("Target body cannot be null");
        }

        Vector2D planetPosition = source.getPosition();
        Vector2D objectPosition = target.getPosition();
        double dx = planetPosition.getX() - objectPosition.getX();
        double dy = planetPosition.getY() - objectPosition.getY();
        double distanceSquared = dx * dx + dy * dy;
        if (distanceSquared == 0) {
            throw new IllegalArgumentException("Source and target cannot occupy the same position");
        }
        double distance = Math.sqrt(distanceSquared);
        double scale = (Constants.GRAVITATIONAL_CONSTANT * source.getMass()) / (distanceSquared * distance);

        double ax = scale * dx;
        double ay = scale * dy;

        return new Vector2D(ax, ay);
    }
}
