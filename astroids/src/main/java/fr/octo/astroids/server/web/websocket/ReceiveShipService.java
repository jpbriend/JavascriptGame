package fr.octo.astroids.server.web.websocket;

import fr.octo.astroids.server.config.ApplicationContextProvider;
import fr.octo.astroids.server.domain.Ship;
import fr.octo.astroids.server.service.GameService;
import fr.octo.astroids.server.service.PlayerService;
import fr.octo.astroids.server.web.websocket.dto.ShipEncoderDecoder;
import org.atmosphere.config.service.*;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;

@ManagedService(path = "/websocket/receiveShipData")
@Singleton
public class ReceiveShipService {

    private final static Logger logger = LoggerFactory.getLogger(ReceiveShipService.class);

    private Broadcaster b = BroadcasterFactory.getDefault().lookup("/websocket/receiveShipData", true);

    private ShipEncoderDecoder shipDecodEncod = new ShipEncoderDecoder();

    private PlayerService playerService;
    private GameService gameService;

    @Ready
    public void onReady(AtmosphereResource r) {
        logger.info("Client connected : " + r.uuid());
        getPlayerService().registerPlayer(r.uuid());
    }

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent event) throws IOException {
        logger.info("Client disconnected : " + event.getResource().uuid());
        getPlayerService().unRegisterPlayer(event.getResource().uuid());
    }

    @Message(decoders = {ShipEncoderDecoder.class})
    public void onMessage(AtmosphereResource atmosphereResource, Ship ship) throws IOException {
        // String message = shipDecodEncod.encode(ship);
        this.getGameService().addShipMessage(ship);

        /*for (AtmosphereResource trackerResource : b.getAtmosphereResources()) {
            trackerResource.getResponse().write(message);
        }*/
    }

    private PlayerService getPlayerService() {
        if (playerService == null) {
            this.playerService = ApplicationContextProvider.getApplicationContext().getBean(PlayerService.class);
        }
        return playerService;
    }

    private GameService getGameService() {
        if (gameService == null) {
            this.gameService = ApplicationContextProvider.getApplicationContext().getBean(GameService.class);
        }
        return gameService;
    }
}
