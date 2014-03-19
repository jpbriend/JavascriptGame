package fr.octo.astroids.server.web.websocket;

import fr.octo.astroids.server.config.ApplicationContextProvider;
import fr.octo.astroids.server.service.CommunicationService;
import fr.octo.astroids.server.service.GameService;
import fr.octo.astroids.server.service.PlayerService;
import fr.octo.astroids.server.web.websocket.dto.ConnectionMessage;
import fr.octo.astroids.server.web.websocket.dto.ConnectionMessageEncoderDecoder;
import org.atmosphere.config.service.*;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@ManagedService(path = "/websocket/connections")
public class ConnectionService {

    private final static Logger logger = LoggerFactory.getLogger(ConnectionService.class);

    private PlayerService playerService;
    private GameService gameService;
    private CommunicationService communicationService;

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent event) {
        // Unregister the player from the game
        getPlayerService().unRegisterPlayer(event.getResource().uuid());
        // Disconnect the player from the game
        this.getCommunicationService().sendDisconnectionMessage(event.getResource().uuid());
    }

    @Message(decoders = {ConnectionMessageEncoderDecoder.class})
    public void onMessage(AtmosphereResource atmosphereResource, ConnectionMessage message) {
        if ("connection".equalsIgnoreCase(message.action)) {
            // Register the player
            getPlayerService().registerPlayer(atmosphereResource.uuid());
            // Notify the player of its ID
            this.getCommunicationService().sendConnectionResponse(atmosphereResource.uuid());
        }
    }

    private CommunicationService getCommunicationService() {
        if (communicationService == null) {
            this.communicationService = ApplicationContextProvider.getApplicationContext().getBean(CommunicationService.class);
        }
        return communicationService;
    }

    private GameService getGameService() {
        if (gameService == null) {
            this.gameService = ApplicationContextProvider.getApplicationContext().getBean(GameService.class);
        }
        return gameService;
    }

    private PlayerService getPlayerService() {
        if (playerService == null) {
            this.playerService = ApplicationContextProvider.getApplicationContext().getBean(PlayerService.class);
        }
        return playerService;
    }

}
