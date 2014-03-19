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
public class ReceiveShipService {

    private GameService gameService;

    @Message(decoders = {ShipEncoderDecoder.class})
    public void onMessage(AtmosphereResource atmosphereResource, Ship ship) throws IOException {
        this.getGameService().addShipMessage(ship);
    }

    private GameService getGameService() {
        if (gameService == null) {
            this.gameService = ApplicationContextProvider.getApplicationContext().getBean(GameService.class);
        }
        return gameService;
    }
}
