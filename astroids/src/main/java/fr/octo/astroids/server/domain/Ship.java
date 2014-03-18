package fr.octo.astroids.server.domain;


import java.util.ArrayList;
import java.util.List;

public class Ship {

    public String id;

    public Double acceleration;

    public Boolean speed;

    public Double dx;

    public Double dy;

    public Double rotation;

    public Double direction;

    public Double x;

    public Double y;

    public String user;

    public List<Bullet> bullets;

    public Boolean isHit;

    public Ship() {
        this.bullets = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ship)) return false;

        Ship ship = (Ship) o;

        if (acceleration != null ? !acceleration.equals(ship.acceleration) : ship.acceleration != null) return false;
        if (bullets != null ? !bullets.equals(ship.bullets) : ship.bullets != null) return false;
        if (direction != null ? !direction.equals(ship.direction) : ship.direction != null) return false;
        if (dx != null ? !dx.equals(ship.dx) : ship.dx != null) return false;
        if (dy != null ? !dy.equals(ship.dy) : ship.dy != null) return false;
        if (id != null ? !id.equals(ship.id) : ship.id != null) return false;
        if (rotation != null ? !rotation.equals(ship.rotation) : ship.rotation != null) return false;
        if (speed != null ? !speed.equals(ship.speed) : ship.speed != null) return false;
        if (user != null ? !user.equals(ship.user) : ship.user != null) return false;
        if (x != null ? !x.equals(ship.x) : ship.x != null) return false;
        if (y != null ? !y.equals(ship.y) : ship.y != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (acceleration != null ? acceleration.hashCode() : 0);
        result = 31 * result + (speed != null ? speed.hashCode() : 0);
        result = 31 * result + (dx != null ? dx.hashCode() : 0);
        result = 31 * result + (dy != null ? dy.hashCode() : 0);
        result = 31 * result + (rotation != null ? rotation.hashCode() : 0);
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        result = 31 * result + (x != null ? x.hashCode() : 0);
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (bullets != null ? bullets.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "id='" + id + '\'' +
                ", acceleration=" + acceleration +
                ", speed=" + speed +
                ", dx=" + dx +
                ", dy=" + dy +
                ", rotation=" + rotation +
                ", direction=" + direction +
                ", x=" + x +
                ", y=" + y +
                ", user='" + user + '\'' +
                ", bullets=" + bullets +
                ", isHit=" + isHit +
                '}';
    }
}
