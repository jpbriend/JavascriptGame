package fr.octo.astroids.server.domain;

public class Vector2 {

    public Double x;
    public Double y;

    public Vector2(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector2)) return false;

        Vector2 vector2 = (Vector2) o;

        if (x != null ? !x.equals(vector2.x) : vector2.x != null) return false;
        if (y != null ? !y.equals(vector2.y) : vector2.y != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
