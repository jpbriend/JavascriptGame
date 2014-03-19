package fr.octo.astroids.server.service;

import fr.octo.astroids.server.domain.Ship;
import fr.octo.astroids.server.web.websocket.dto.ConnectionMessage;
import fr.octo.astroids.server.web.websocket.dto.ConnectionMessageEncoderDecoder;
import fr.octo.astroids.server.web.websocket.dto.ShipEncoderDecoder;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Communication service in charge of sending messages to clients via websockets.
 */
@Service
public class CommunicationService {

    private Map<String, Broadcaster> broadcasters = new HashMap<>();

    private ShipEncoderDecoder shipDecodEncod = new ShipEncoderDecoder();
    private ConnectionMessageEncoderDecoder connectionMessageEncoderDecoder = new ConnectionMessageEncoderDecoder();

    private Broadcaster getBroadcaster(String broadcaster) {
        if (this.broadcasters.get(broadcaster) == null) {
            broadcasters.put(broadcaster, BroadcasterFactory.getDefault().lookup(broadcaster, true));
        }
        return this.broadcasters.get(broadcaster);
    }

    public void sendShips(Collection<Ship> ships) {
        for (Ship s : ships) {
            String message = shipDecodEncod.encode(s);
            for (AtmosphereResource trackerResource : this.getBroadcaster("/websocket/receiveShipData").getAtmosphereResources()) {
                trackerResource.getResponse().write(message);
            }
        }

    }

    /**
     * Broadcast a message indicating a player has left the game
     * @param clientId
     */
    public void sendDisconnectionMessage(String clientId) {
        ConnectionMessage m = new ConnectionMessage("disconnected", clientId);
        String message = connectionMessageEncoderDecoder.encode(m);
        for (AtmosphereResource trackerResource : this.getBroadcaster("/websocket/connections").getAtmosphereResources()) {
            trackerResource.getResponse().write(message);
        }
    }

    /**
     * Sends the player its ID for the game
     * @param clientId
     */
    public void sendConnectionResponse(String clientId) {
        for (AtmosphereResource trackerResource : this.getBroadcaster("/websocket/connections").getAtmosphereResources()) {
            if (trackerResource.uuid().equals(clientId)) {
                ConnectionMessage message = new ConnectionMessage("connected", clientId);
                trackerResource.getResponse().write(connectionMessageEncoderDecoder.encode(message));
                return;
            }
        }
    }
}
