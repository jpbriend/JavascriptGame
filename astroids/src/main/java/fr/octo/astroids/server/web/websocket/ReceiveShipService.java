package fr.octo.astroids.server.web.websocket;

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

@ManagedService(path = "/websocket/recieveShipData")
public class ReceiveShipService {

    private final static Logger logger = LoggerFactory.getLogger(ReceiveShipService.class);

    private Broadcaster b =
            BroadcasterFactory.getDefault().lookup("/websocket/recieveShipData", true);

    @Ready
    public void onReady(AtmosphereResource r) {
        logger.info("Client connected : " + r.uuid());
    }

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent event) throws IOException {
        logger.info("Client disconnected : " + event.getResource().uuid());
    }

    @Message// (decoders = {ActivityDTOJacksonDecoder.class})
    public void onMessage(AtmosphereResource atmosphereResource, Object object) throws IOException {
        // logger.info("message received : " + object);
        for (AtmosphereResource trackerResource : b.getAtmosphereResources()) {
            trackerResource.getResponse().write(object.toString());
        }
    }
}
