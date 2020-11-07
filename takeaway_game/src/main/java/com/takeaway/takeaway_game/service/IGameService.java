package com.takeaway.takeaway_game.service;

import java.util.Map;

public interface IGameService {

	Integer registerGame(String playerId);

	Map<String, Integer> getActivePlayers(String playerId);

}
