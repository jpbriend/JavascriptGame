package fr.octo.astroids.server.web.websocket.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.atmosphere.config.managed.Decoder;
import org.atmosphere.config.managed.Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ConnectionMessageEncoderDecoder implements Encoder<ConnectionMessage, String>, Decoder<String, ConnectionMessage> {

    private static final Logger log = LoggerFactory.getLogger(ConnectionMessageEncoderDecoder.class);

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Override
    public ConnectionMessage decode(String s) {
        try {
            return jsonMapper.readValue(s, ConnectionMessage.class);
        } catch (IOException e) {
            log.error("Error while decoding the String: {}", s, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String encode(ConnectionMessage s) {
        try {
            return jsonMapper.writeValueAsString(s);
        } catch (IOException e) {
            log.error("Error while encoding the String: {}", e);
            throw new RuntimeException(e);
        }
    }
}
