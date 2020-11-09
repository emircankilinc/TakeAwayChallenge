package com.takeaway.takeaway_game.service;

import java.util.Map;

import com.takeaway.takeaway_game.entity.Player;

public interface IGameService {

	Integer registerGame(String playerId);

	Map<String, Integer> getActivePlayers(String playerId);

	Boolean startGame(Player player);

}
