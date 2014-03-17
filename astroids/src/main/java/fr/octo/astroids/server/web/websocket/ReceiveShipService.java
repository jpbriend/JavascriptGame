package fr.octo.astroids.server.web.websocket;

import fr.octo.astroids.server.domain.Ship;
import fr.octo.astroids.server.web.websocket.dto.ShipEncoderDecoder;
import org.atmosphere.config.service.Disconnect;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.Message;
import org.atmosphere.config.service.Ready;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@ManagedService(path = "/websocket/receiveShipData")
public class ReceiveShipService {

    private final static Logger logger = LoggerFactory.getLogger(ReceiveShipService.class);

    private Broadcaster b = BroadcasterFactory.getDefault().lookup("/websocket/receiveShipData", true);

    private ShipEncoderDecoder shipDecodEncod = new ShipEncoderDecoder();


    @Ready
    public void onReady(AtmosphereResource r) {
        logger.info("Client connected : " + r.uuid());
    }

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent event) throws IOException {
        logger.info("Client disconnected : " + event.getResource().uuid());
    }

    @Message(decoders = {ShipEncoderDecoder.class})
    public void onMessage(AtmosphereResource atmosphereResource, Ship ship) throws IOException {
        String message = shipDecodEncod.encode(ship);

        for (AtmosphereResource trackerResource : b.getAtmosphereResources()) {
            trackerResource.getResponse().write(message);
        }
    }
}
