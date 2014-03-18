package fr.octo.astroids.server.domain;

public class ServerSideShip {
    public String id;

    public Triangle coordinates;

    public ServerSideShip(String id, Triangle coordinates) {
        this.id = id;
        this.coordinates = coordinates;
    }
}
