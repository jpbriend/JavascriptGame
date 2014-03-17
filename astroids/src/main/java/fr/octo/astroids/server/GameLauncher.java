package fr.octo.astroids.server;

import fr.octo.astroids.server.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Launches the endless Game loop at the server startup.
 */
@Component
public class GameLauncher {

    @Autowired
    private GameService gameService;

    @PostConstruct
    public void launchGameLoop() {
        gameService.gameLoop();
    }

}
