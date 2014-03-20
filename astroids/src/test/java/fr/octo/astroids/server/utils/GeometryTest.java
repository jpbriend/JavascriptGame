package fr.octo.astroids.server.utils;

import fr.octo.astroids.server.domain.Triangle;
import fr.octo.astroids.server.domain.Vector2;
import org.junit.Test;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;


public class GeometryTest {
    @Test
    public void testIsInsideTriangle() throws Exception {
        Vector2 p = new Vector2(2d, 3d);
        Vector2 pA = new Vector2(1d, 1d);
        Vector2 pB = new Vector2(4d, 2d);
        Vector2 pC = new Vector2(2d, 7d);

        assertThat(Geometry.isInsideTriangle(p, pA, pB, pC)).isTrue();
    }

    @Test
    public void testIsNotInsideTriangle() throws Exception {
        Vector2 p = new Vector2(1.5d, 5d);
        Vector2 pA = new Vector2(1d, 1d);
        Vector2 pB = new Vector2(4d, 2d);
        Vector2 pC = new Vector2(2d, 7d);

        assertThat(Geometry.isInsideTriangle(p, pA, pB, pC)).isFalse();
    }

    @Test
    public void testIsInsideTriangle2() throws Exception {
        Vector2 p = new Vector2(2d, 3d);
        Vector2 pA = new Vector2(1d, 1d);
        Vector2 pB = new Vector2(4d, 2d);
        Vector2 pC = new Vector2(2d, 7d);
        Triangle triangle = new Triangle(pA, pB, pC);

        assertThat(Geometry.isInsideTriangle(p, triangle)).isTrue();
    }

    @Test
    public void testIsNotInsideTriangle2() throws Exception {
        Vector2 p = new Vector2(1.5d, 5d);
        Vector2 pA = new Vector2(1d, 1d);
        Vector2 pB = new Vector2(4d, 2d);
        Vector2 pC = new Vector2(2d, 7d);
        Triangle triangle = new Triangle(pA, pB, pC);

        assertThat(Geometry.isInsideTriangle(p, triangle)).isFalse();
    }

    @Test
    public void testCoordinatesAfterRotation() {
        Vector2 point = new Vector2(2d, 0d);
        Double rotation = 50 * Math.PI / 180;// rotation of 50 degrees
        DecimalFormat df = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH));
        assertThat(df.format(Geometry.coordinatesAfterRotation(point, rotation).x)).isEqualTo("1.29");
        assertThat(df.format(Geometry.coordinatesAfterRotation(point, rotation).y)).isEqualTo("1.53");
    }
}
