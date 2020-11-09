package com.takeaway.takeaway_game.service;

import java.util.HashMap;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.takeaway.takeaway_game.entity.Player;
import com.takeaway.takeaway_game.server.GameServerProducer;

@RestController
public class GameService implements IGameService {

	private static final HashMap<String, Integer> registeredPlayers = new HashMap<String, Integer>();

	@Autowired
	private GameServerProducer gameServerProducer;

	@Autowired
	private RabbitTemplate rabbittemplate;

	private static ObjectMapper obj;

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
		clonedMap.remove(playerId);
		return clonedMap;
	}

	@GetMapping("/start")
	@Override
	public Boolean startGame(@PathParam("player") String player) {
		try {
			Player readValue = new ObjectMapper().readValue(player, Player.class);
			String writeValueAsString = obj.writeValueAsString(readValue);
			rabbittemplate.convertAndSend("x.game", readValue.getRivalId().toString(), writeValueAsString);
		} catch (JsonMappingException e) {
			return Boolean.FALSE;
		} catch (JsonProcessingException e) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

}
