package fr.octo.astroids.server.web.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.octo.astroids.server.web.websocket.dto.ActivityDTO;
import fr.octo.astroids.server.web.websocket.dto.ActivityDTOJacksonDecoder;
import org.atmosphere.config.service.Disconnect;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.Message;
import org.atmosphere.cpr.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Calendar;

@ManagedService(
        path = "/websocket/activity")
public class ActivityService {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private Broadcaster b =
            BroadcasterFactory.getDefault().lookup("/websocket/tracker", true);

    private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    private ObjectMapper jsonMapper = new ObjectMapper();

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent event) throws IOException {
        log.debug("Browser {} disconnected", event.getResource().uuid());
        AtmosphereRequest request = event.getResource().getRequest();
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setUuid(event.getResource().uuid());
        activityDTO.setPage("logout");
        String json = jsonMapper.writeValueAsString(activityDTO);
        for (AtmosphereResource trackerResource : b.getAtmosphereResources()) {
            trackerResource.getResponse().write(json);
        }
    }

    @Message(decoders = {ActivityDTOJacksonDecoder.class})
    public void onMessage(AtmosphereResource atmosphereResource, ActivityDTO activityDTO) throws IOException {
        AtmosphereRequest request = atmosphereResource.getRequest();
        activityDTO.setUuid(atmosphereResource.uuid());
        activityDTO.setIpAddress(request.getRemoteAddr());
        activityDTO.setTime(dateTimeFormatter.print(Calendar.getInstance().getTimeInMillis()));
        String json = jsonMapper.writeValueAsString(activityDTO);
        log.debug("Sending user tracking data {}", json);
        for (AtmosphereResource trackerResource : b.getAtmosphereResources()) {
            trackerResource.getResponse().write(json);
        }
    }
}
