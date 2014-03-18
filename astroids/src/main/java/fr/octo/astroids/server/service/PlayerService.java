package fr.octo.astroids.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class PlayerService  {
    private final static Logger logger = LoggerFactory.getLogger(PlayerService.class);

    private List<String> players = new ArrayList<>();

    // TODO Backup the Set by Redis maybe
    public void registerPlayer(String playerId) {
        if (players.contains(playerId)) {
            logger.info("Player " + playerId + " already connected. Maybe it's a reconnect.");
        } else {
            logger.info("New player detected. Registering it : " + playerId);
            this.players.add(playerId);
        }
    }

    public void unRegisterPlayer(String playerId) {
        if (players.contains(playerId)) {
            logger.info("Unregistering player " + playerId);
            this.players.remove(playerId);
        } else {
            logger.info("Can not find player " + playerId);
        }
    }
}
