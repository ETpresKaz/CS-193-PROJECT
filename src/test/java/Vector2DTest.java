import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Vector2DTest {

    private static final double EPSILON = 1e-9;

// constructors and getters

    @Test
    public void testConstructorAndGetters() {
        Vector2D vector = new Vector2D(3.5, -7.2);
        assertEquals(3.5, vector.getX());
        assertEquals(-7.2, vector.getY());
    }

    @Test
    public void testZeroVector() {
        Vector2D vector = new Vector2D(0, 0);
        assertEquals(0.0, vector.getX());
        assertEquals(0.0, vector.getY());
        assertEquals(0.0, vector.magnitude());
        assertEquals(0.0, vector.magnitudeSquared());
    }

// magnitude and magnitudeSquared

    @Test
    public void testMagnitude() {
        Vector2D vector = new Vector2D(3, 4);
        assertEquals(5.0, vector.magnitude(), EPSILON);
    }

    @Test
    public void testMagnitudeNegativeComponents() {
        Vector2D vector = new Vector2D(-6, -8);
        assertEquals(10.0, vector.magnitude(), EPSILON);
    }

    @Test
    public void testMagnitudeSquared() {
        Vector2D vector = new Vector2D(3, 4);
        assertEquals(25.0, vector.magnitudeSquared(), EPSILON);
    }

// addition

    @Test
    public void testAddition() {
        Vector2D a = new Vector2D(3, 4);
        Vector2D b = new Vector2D(1, 2);
        Vector2D result = a.add(b);
        assertEquals(4.0, result.getX(), EPSILON);
        assertEquals(6.0, result.getY(), EPSILON);
    }

    @Test
    public void testAdditionWithNegativeValues() {
        Vector2D a = new Vector2D(5, -3);
        Vector2D b = new Vector2D(-2, 7);
        Vector2D result = a.add(b);
        assertEquals(3.0, result.getX(), EPSILON);
        assertEquals(4.0, result.getY(), EPSILON);
    }

    @Test
    public void testAdditionWithNull() {
        Vector2D vector = new Vector2D(1, 2);
        assertThrows(IllegalArgumentException.class, () -> {
            vector.add(null);
        });
    }

// subtraction

    @Test
    public void testSubtraction() {
        Vector2D a = new Vector2D(5, 8);
        Vector2D b = new Vector2D(2, 3);
        Vector2D result = a.subtract(b);
        assertEquals(3.0, result.getX(), EPSILON);
        assertEquals(5.0, result.getY(), EPSILON);
    }

    @Test
    public void testSubtractSelf() {
        Vector2D vector = new Vector2D(7, -4);
        Vector2D result = vector.subtract(vector);
        assertEquals(0.0, result.getX(), EPSILON);
        assertEquals(0.0, result.getY(), EPSILON);
    }

    @Test
    public void testSubtractionWithNull() {
        Vector2D vector = new Vector2D(1, 2);
        assertThrows(IllegalArgumentException.class, () -> {
            vector.subtract(null);
        });
    }

// scaling

    @Test
    public void testScale() {
        Vector2D vector = new Vector2D(3, 4);
        Vector2D result = vector.scale(2);
        assertEquals(6.0, result.getX(), EPSILON);
        assertEquals(8.0, result.getY(), EPSILON);
    }

    @Test
    public void testScaleNegative() {
        Vector2D vector = new Vector2D(3, -4);
        Vector2D result = vector.scale(-2);
        assertEquals(-6.0, result.getX(), EPSILON);
        assertEquals(8.0, result.getY(), EPSILON);
    }

    @Test
    public void testScaleZero() {
        Vector2D vector = new Vector2D(3, 4);
        Vector2D result = vector.scale(0);
        assertEquals(0.0, result.getX(), EPSILON);
        assertEquals(0.0, result.getY(), EPSILON);
    }

// normalization

    @Test
    public void testNormalize() {
        Vector2D vector = new Vector2D(3, 4);
        Vector2D result = vector.normalize();
        assertEquals(0.6, result.getX(), EPSILON);
        assertEquals(0.8, result.getY(), EPSILON);
        assertEquals(1.0, result.magnitude(), EPSILON);
    }

    @Test
    public void testNormalizeNegativeVector() {
        Vector2D vector = new Vector2D(-3, -4);
        Vector2D result = vector.normalize();
        assertEquals(-0.6, result.getX(), EPSILON);
        assertEquals(-0.8, result.getY(), EPSILON);
    }

    @Test
    public void testNormalizeZeroVector() {
        Vector2D vector = new Vector2D(0, 0);
        assertThrows(ArithmeticException.class, () -> {
            vector.normalize();
        });
    }

// dot product

    @Test
    public void testDotProduct() {
        Vector2D a = new Vector2D(3, 4);
        Vector2D b = new Vector2D(2, 5);
        assertEquals(26.0, a.dot(b), EPSILON);
    }

    @Test
    public void testDotProductPerpendicular() {
        Vector2D a = new Vector2D(1, 0);
        Vector2D b = new Vector2D(0, 1);
        assertEquals(0.0, a.dot(b), EPSILON);
    }

    @Test
    public void testDotProductNull() {
        Vector2D vector = new Vector2D(1, 2);
        assertThrows(IllegalArgumentException.class, () -> {
            vector.dot(null);
        });
    }

// cross product

    @Test
    public void testCrossProduct() {
        Vector2D a = new Vector2D(3, 4);
        Vector2D b = new Vector2D(2, 5);
        assertEquals(7.0, a.cross(b), EPSILON);
    }

    @Test
    public void testCrossProductParallel() {
        Vector2D a = new Vector2D(2, 4);
        Vector2D b = new Vector2D(4, 8);
        assertEquals(0.0, a.cross(b), EPSILON);
    }

    @Test
    public void testCrossProductDirection() {
        Vector2D a = new Vector2D(1, 0);
        Vector2D b = new Vector2D(0, 1);
        assertEquals(1.0, a.cross(b), EPSILON);
        assertEquals(-1.0, b.cross(a), EPSILON);
    }

    @Test
    public void testCrossProductNull() {
        Vector2D vector = new Vector2D(1, 2);
        assertThrows(IllegalArgumentException.class, () -> {
            vector.cross(null);
        });
    }

// distance calculations

    @Test
    public void testDistanceTo() {
        Vector2D a = new Vector2D(0, 0);
        Vector2D b = new Vector2D(3, 4);
        assertEquals(5.0, a.distanceTo(b), EPSILON);
    }

    @Test
    public void testDistanceSymmetry() {
        Vector2D a = new Vector2D(10, -5);
        Vector2D b = new Vector2D(-3, 7);
        assertEquals(a.distanceTo(b), b.distanceTo(a), EPSILON);
    }

    @Test
    public void testDistanceToSelf() {
        Vector2D vector = new Vector2D(5, 9);
        assertEquals(0.0, vector.distanceTo(vector), EPSILON);
    }

    @Test
    public void testDistanceSquaredTo() {
        Vector2D a = new Vector2D(0, 0);
        Vector2D b = new Vector2D(3, 4);
        assertEquals(25.0, a.distanceSquaredTo(b), EPSILON);
    }

    @Test
    public void testDistanceToNull() {
        Vector2D vector = new Vector2D(1, 2);
        assertThrows(IllegalArgumentException.class, () -> {
            vector.distanceTo(null);
        });
    }

    @Test
    public void testDistanceSquaredToNull() {
        Vector2D vector = new Vector2D(1, 2);
        assertThrows(IllegalArgumentException.class, () -> {
            vector.distanceSquaredTo(null);
        });
    }

// perpendicular vector

    @Test
    public void testPerpendicular() {
        Vector2D vector = new Vector2D(3, 4);
        Vector2D result = vector.perpendicular();
        assertEquals(-4.0, result.getX(), EPSILON);
        assertEquals(3.0, result.getY(), EPSILON);
    }

    @Test
    public void testPerpendicularDotProduct() {
        Vector2D vector = new Vector2D(3, 4);
        assertEquals(0.0, vector.dot(vector.perpendicular()), EPSILON);
    }

// negation

    @Test
    public void testNegate() {
        Vector2D vector = new Vector2D(3, -4);
        Vector2D result = vector.negate();
        assertEquals(-3.0, result.getX(), EPSILON);
        assertEquals(4.0, result.getY(), EPSILON);
    }

    @Test
    public void testDoubleNegation() {
        Vector2D vector = new Vector2D(3, -4);
        Vector2D result = vector.negate().negate();
        assertEquals(vector.getX(), result.getX(), EPSILON);
        assertEquals(vector.getY(), result.getY(), EPSILON);
    }

// angle

    @Test
    public void testAngle90Degrees() {
        Vector2D a = new Vector2D(1, 0);
        Vector2D b = new Vector2D(0, 1);
        assertEquals(Math.PI / 2, a.angle(b), EPSILON);
    }

    @Test
    public void testAngle180Degrees() {
        Vector2D a = new Vector2D(1, 0);
        Vector2D b = new Vector2D(-1, 0);
        assertEquals(Math.PI, a.angle(b), EPSILON);
    }

    @Test
    public void testAngleParallelVectors() {
        Vector2D a = new Vector2D(1, 1);
        Vector2D b = new Vector2D(2, 2);
        assertEquals(0.0, a.angle(b), EPSILON);
    }

    @Test
    public void testAngleWithZeroVector() {
        Vector2D a = new Vector2D(1, 0);
        Vector2D b = new Vector2D(0, 0);
        assertThrows(ArithmeticException.class, () -> {
            a.angle(b);
        });
    }

    @Test
    public void testAngleWithNull() {
        Vector2D vector = new Vector2D(1, 0);
        assertThrows(IllegalArgumentException.class, () -> {
            vector.angle(null);
        });
    }

 // rotation

    @Test
    public void testRotate90Degrees() {
        Vector2D vector = new Vector2D(1, 0);
        Vector2D result = vector.rotate(Math.PI / 2);
        assertEquals(0.0, result.getX(), EPSILON);
        assertEquals(1.0, result.getY(), EPSILON);
    }

    @Test
    public void testRotate180Degrees() {
        Vector2D vector = new Vector2D(1, 0);
        Vector2D result = vector.rotate(Math.PI);
        assertEquals(-1.0, result.getX(), EPSILON);
        assertEquals(0.0, result.getY(), EPSILON);
    }

    @Test
    public void testRotate360Degrees() {
        Vector2D vector = new Vector2D(3, 4);
        Vector2D result = vector.rotate(2 * Math.PI);
        assertEquals(3.0, result.getX(), EPSILON);
        assertEquals(4.0, result.getY(), EPSILON);
    }

    @Test
    public void testRotationPreservesMagnitude() {
        Vector2D vector = new Vector2D(3, 4);
        Vector2D result = vector.rotate(Math.PI / 3);
        assertEquals(vector.magnitude(), result.magnitude(), EPSILON);
    }

// immutability tests

    @Test
    public void testAdditionDoesNotModifyOriginal() {
        Vector2D a = new Vector2D(3, 4);
        Vector2D b = new Vector2D(1, 2);
        a.add(b);
        assertEquals(3.0, a.getX());
        assertEquals(4.0, a.getY());
    }

    @Test
    public void testScaleDoesNotModifyOriginal() {
        Vector2D vector = new Vector2D(3, 4);
        vector.scale(10);
        assertEquals(3.0, vector.getX());
        assertEquals(4.0, vector.getY());
    }

    @Test
    public void testRotateDoesNotModifyOriginal() {
        Vector2D vector = new Vector2D(3, 4);
        vector.rotate(Math.PI / 2);
        assertEquals(3.0, vector.getX());
        assertEquals(4.0, vector.getY());
    }

// String representation

    @Test
    public void testToString() {
        Vector2D vector = new Vector2D(3, 4);
        assertEquals("Vector2D(3.0, 4.0)", vector.toString());
    }
}