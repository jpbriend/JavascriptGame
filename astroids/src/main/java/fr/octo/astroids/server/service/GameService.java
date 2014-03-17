package fr.octo.astroids.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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

    /**
     * Tick() is executed each XXXms and updates the game system according to users inputs.
     */
    private void tick(long elapsedTime) {

    }

    private void initialize() {
        logger.info("Initializing Game system...");

        logger.info("Game system initialized.");
    }
}
