package fr.octo.astroids.server.domain;

import fr.octo.astroids.server.utils.Geometry;

public class Triangle {
    public Vector2 pointA;
    public Vector2 pointB;
    public Vector2 pointC;

    public Triangle() {
        this.pointA = new Vector2(0d, 0d);
        this.pointB = new Vector2(0d, 0d);
        this.pointC = new Vector2(0d, 0d);
    }

    public Triangle(Vector2 pointA, Vector2 pointB, Vector2 pointC) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.pointC = pointC;
    }

    public Triangle(Vector2 center, Vector2 coordA, Vector2 coordB, Vector2 coordC, Double rotation) {
        Vector2 coordAAfterRotation = Geometry.coordinatesAfterRotation(coordA, rotation);
        Vector2 coordBAfterRotation = Geometry.coordinatesAfterRotation(coordB, rotation);
        Vector2 coordCAfterRotation = Geometry.coordinatesAfterRotation(coordC, rotation);

        this.pointA = new Vector2(
                center.x + coordAAfterRotation.x,
                center.y + coordAAfterRotation.y);
        this.pointB = new Vector2(
                center.x + coordBAfterRotation.x,
                center.y + coordBAfterRotation.y);
        this.pointC = new Vector2(
                center.x + coordCAfterRotation.x,
                center.y + coordCAfterRotation.y);

    }


}
