package fr.octo.astroids.server.web.websocket.dto;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import fr.octo.astroids.server.domain.Ship;
import org.atmosphere.config.managed.Decoder;
import org.atmosphere.config.managed.Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ShipEncoderDecoder implements Encoder<Ship, String>, Decoder<String, Ship> {
    private static final Logger log = LoggerFactory.getLogger(ShipEncoderDecoder.class);

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Override
    public String encode(Ship s) {
        try {
            return jsonMapper.writeValueAsString(s);
        } catch (IOException e) {
            log.error("Error while encoding the String: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Ship decode(String s) {
        try {
            return jsonMapper.readValue(s, Ship.class);
        } catch (IOException e) {
            log.error("Error while decoding the String: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
