package fr.octo.astroids.server.domain;


public class Bullet {

    public Long x;

    public Long y;

    public Long direction;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bullet bullet = (Bullet) o;

        if (direction != null ? !direction.equals(bullet.direction) : bullet.direction != null) return false;
        if (x != null ? !x.equals(bullet.x) : bullet.x != null) return false;
        if (y != null ? !y.equals(bullet.y) : bullet.y != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        return result;
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public Long getDirection() {
        return direction;
    }

    public void setDirection(Long direction) {
        this.direction = direction;
    }
}
