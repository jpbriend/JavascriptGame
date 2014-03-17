package fr.octo.astroids.server.web.websocket.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.atmosphere.config.managed.Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ActivityDTOJacksonDecoder implements Decoder<String, ActivityDTO> {

    private static final Logger log = LoggerFactory.getLogger(ActivityDTOJacksonDecoder.class);

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Override
    public ActivityDTO decode(String jsonString) {
        try {
            return jsonMapper.readValue(jsonString, ActivityDTO.class);
        } catch (IOException e) {
            log.error("Error while decoding the String: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
