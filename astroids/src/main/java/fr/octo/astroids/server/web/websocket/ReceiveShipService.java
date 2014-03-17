package fr.octo.astroids.server.web.websocket;

import fr.octo.astroids.server.domain.Ship;
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

import javax.inject.Inject;
import java.io.IOException;

@ManagedService(path = "/websocket/receiveShipData")
@Singleton
public class ReceiveShipService implements ApplicationContextAware{

    private final static Logger logger = LoggerFactory.getLogger(ReceiveShipService.class);

    private Broadcaster b = BroadcasterFactory.getDefault().lookup("/websocket/receiveShipData", true);

    private ShipEncoderDecoder shipDecodEncod = new ShipEncoderDecoder();

    // F...g DI does not work with ManagedService (despite everything tried)
    private ApplicationContext applicationContext;

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
        String message = shipDecodEncod.encode(ship);

        for (AtmosphereResource trackerResource : b.getAtmosphereResources()) {
            trackerResource.getResponse().write(message);
        }
    }

    private PlayerService getPlayerService() {
        return applicationContext.getBean(PlayerService.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
