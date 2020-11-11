package com.takeaway.takeaway_game.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.websocket.server.PathParam;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.takeaway.takeaway_game.entity.Player;

@RestController
public class GameService implements IGameService {

	private static final HashMap<String, Integer> registeredPlayers = new HashMap<String, Integer>();

	@Autowired
	private RabbitTemplate rabbittemplate;

	@GetMapping("/register")
	@Override
	public Integer registerGame(@PathParam("playerId") String playerId) {
		if (registeredPlayers.containsKey(playerId)) {
			return registeredPlayers.get(playerId);
		}
		int size = registeredPlayers.size();
		registeredPlayers.put(playerId, size);
		return size;
	}

	@GetMapping("/active")
	@Override
	public Map<String, Integer> getActivePlayers(@PathParam("playerId") String playerId) {
		HashMap<String, Integer> clonedMap = (HashMap<String, Integer>) registeredPlayers.clone();
		for (Entry<String, Integer> entry : clonedMap.entrySet()) {
			if (playerId.equals(entry.getValue().toString())) {
				clonedMap.remove(entry.getKey());
				return clonedMap;
			}
		}
		return clonedMap;
	}

	@PostMapping(value = "/start", consumes = "application/json", produces = "application/json")
	@Override
	public Boolean startGame(@RequestBody Player player) {
		try {
			rabbittemplate.convertAndSend("x.game", player.getPlayerId().toString(),
					new ObjectMapper().writeValueAsString(player));
		} catch (AmqpException e) {
			return Boolean.FALSE;
		} catch (JsonProcessingException e) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

}
