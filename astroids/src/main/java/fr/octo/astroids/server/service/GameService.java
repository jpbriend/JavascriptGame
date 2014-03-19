package fr.octo.astroids.server.service;

import fr.octo.astroids.server.domain.ServerSideShip;
import fr.octo.astroids.server.domain.Ship;
import fr.octo.astroids.server.domain.Triangle;
import fr.octo.astroids.server.domain.Vector2;
import fr.octo.astroids.server.utils.Geometry;
import fr.octo.astroids.server.web.websocket.dto.ShipEncoderDecoder;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * This service contains the main loop of the game.
 * It is executed at server startup and it continuously loop.
 */
@Service
public class GameService {

    private final static Logger logger = LoggerFactory.getLogger(GameService.class);

    private final static Integer FRAMES_PER_SECOND = 30;

    private Integer maxWorkingTimePerFrame = 1000 / FRAMES_PER_SECOND;

    @Async
    public void gameLoop() {
        logger.info("Game loop has started.");

        // Init
        initialize();

        long lastStartTime = System.nanoTime();

        // Main loop
        while (true) {
            long elapsedTime = System.nanoTime() - lastStartTime;
            lastStartTime = System.nanoTime();

            // Tick
            tick(elapsedTime);

            // Have a break, have a Kitkat
            long processingTimeForCurrentFrame = System.nanoTime() - lastStartTime;
            if (processingTimeForCurrentFrame < maxWorkingTimePerFrame) {
                try {
                    Thread.sleep(maxWorkingTimePerFrame - processingTimeForCurrentFrame);
                } catch(Exception e) {
                    logger.error("Error while sleeping in the main game loop " + e);
                }
            }
        }
    }

    /**********************************************************************************************************/

    private Queue<Ship> shipQueue = new LinkedList<>();

    // TODO : move to a communication service
    private Broadcaster b = null;
    private ShipEncoderDecoder shipDecodEncod = new ShipEncoderDecoder();

    /**
     * Tick() is executed each XXXms and updates the game system according to users inputs.
     */
    private void tick(long elapsedTime) {
        List<Ship> shipsAfterCollisionDetection = checkCollisions();

        Map<String, Ship> ships = processShipMessagesToServerSideShips(shipsAfterCollisionDetection);

        // Send these events to all clients
        for (Ship s : ships.values()) {
            String message = shipDecodEncod.encode(s);
            for (AtmosphereResource trackerResource : getBroadcaster("/websocket/receiveShipData").getAtmosphereResources()) {
                trackerResource.getResponse().write(message);
            }
        }
    }

    private List<Ship> checkCollisions() {
        List<Ship> ships = new ArrayList<>(this.shipQueue.size());
        Ship s = null;

        while((s = this.shipQueue.poll()) != null) {

            // Check collision with previous ships
            Triangle sTriangle = new Triangle(new Vector2(s.x, s.y), new Vector2(0d, -5d), new Vector2(-3d, 5d), new Vector2(3d, 5d), s.rotation);
            for (Ship sh : ships) {
                // Do not check collision with yourself
                if (!sh.user.equalsIgnoreCase(s.user)) {
                    Triangle shTriangle = new Triangle(new Vector2(sh.x, sh.y), new Vector2(0d, -5d), new Vector2(-3d, 5d), new Vector2(3d, 5d), sh.rotation);
                    if (Geometry.areTrianglesColliding(sTriangle, shTriangle)) {
                        s.isHit = true;
                    }
                }
            }

            ships.add(s);
        }

        return ships;
    }

    /**
     * Map contains only the last event for a given ship (because put overrides the previous ones).
     * @return
     */
    private Map<String, Ship> processShipMessagesToServerSideShips(List<Ship> shipsAfterCollisionDetection) {
        Map<String, Ship> retour = new HashMap<>();
        for (Ship s : shipsAfterCollisionDetection) {
            // logger.info("Processing Ship message : " + s.toString());
            // Triangle t = new Triangle(new Vector2(s.x, s.y), new Vector2(0d, -5d), new Vector2(-3d, 5d), new Vector2(3d, 5d), s.rotation);
            // ServerSideShip ship = new ServerSideShip(s.user, t);
            retour.put(s.user, s);
        }

        return retour;
    }

    private Broadcaster getBroadcaster(String websocket) {
        if (b == null) {
            b = BroadcasterFactory.getDefault().lookup(websocket, true);
        }
        return b;
    }

    public void addShipMessage(Ship ship) {
        this.shipQueue.add(ship);
    }

    private void initialize() {
        logger.info("Initializing Game system...");

        logger.info("Game system initialized.");
    }
}
