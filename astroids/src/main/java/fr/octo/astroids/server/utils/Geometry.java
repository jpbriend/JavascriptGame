package fr.octo.astroids.server.utils;


import fr.octo.astroids.server.domain.Triangle;
import fr.octo.astroids.server.domain.Vector2;

public class Geometry {

    /**
     * Determines if p is inside the triangle made of pA, pB and pC.
     * @return
     */
    public static boolean isInsideTriangle(Vector2 p, Vector2 pA, Vector2 pB, Vector2 pC) {
        // Move origin of coordinates to point A
        Vector2 pointB = new Vector2(pB.x - pA.x, pB.y - pA.y);
        Vector2 pointC = new Vector2(pC.x - pA.x, pC.y - pA.y);
        Vector2 point = new Vector2(p.x - pA.x, p.y - pA.y);

        // Calculate scalar
        Double scalar = pointB.x * pointC.y - pointC.x * pointB.y;

        //  Calculate the 3 Barycentric weights
        Double weightA = ( point.x * ( pointB.y - pointC.y ) + point.y * ( pointC.x - pointB.x ) + pointB.x * pointC.y - pointC.x * pointB.y ) / scalar;

        Double weightB = ( point.x * pointC.y - point.y * pointC.x ) / scalar;

        Double weightC = ( point.y * pointB.x - point.x * pointB.y ) / scalar;

        // Point is inside triangle if and only if the 3 weights are between 0 and 1
        return isBetweenZeroAndOne(weightA) && isBetweenZeroAndOne(weightB) && isBetweenZeroAndOne(weightC);
    }

    public static boolean isInsideTriangle(Vector2 p, Triangle triangle) {
        return isInsideTriangle(p, triangle.pointA, triangle.pointB, triangle.pointC);
    }

    private static boolean isBetweenZeroAndOne(Double d) {
        return d <= 1 && d >= 0;
    }

    public static boolean areTrianglesColliding(Triangle triangle1, Triangle triangle2) {
        return isInsideTriangle(triangle1.pointA, triangle2)
                || isInsideTriangle(triangle1.pointB, triangle2)
                || isInsideTriangle(triangle1.pointC, triangle2);
    }

    public static Vector2 coordinatesAfterRotation(Vector2 coordinates, Double rotation) {
        return new Vector2(
                coordinates.x * Math.cos(rotation) - coordinates.y * Math.sin(rotation),
                coordinates.x * Math.sin(rotation) - coordinates.y * Math.cos(rotation));
    }
}
