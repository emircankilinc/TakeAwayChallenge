package com.takeaway.takeaway_game.service;

import java.util.HashMap;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameService implements IGameService {

	private static final HashMap<String, Integer> registeredPlayers = new HashMap<String, Integer>();

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

}
