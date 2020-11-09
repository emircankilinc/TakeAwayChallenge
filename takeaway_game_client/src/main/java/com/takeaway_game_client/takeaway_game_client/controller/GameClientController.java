package com.takeaway_game_client.takeaway_game_client.controller;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.takeaway_game_client.takeaway_game_client.entity.Player;

public class GameClientController {

	public Integer registerGame() {
		byte[] array = new byte[7]; // length is bounded by 7
		new Random().nextBytes(array);
		String generatedString = new String(array, Charset.forName("UTF-8"));
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> call = restTemplate
				.getForEntity("http://localhost:5555/register?playerId=" + generatedString, String.class);
		return Integer.valueOf(call.getBody());
	}

	public Collection<Integer> getActivePlayers(Integer playerId) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Map> call = restTemplate.getForEntity("http://localhost:5555/active?playerId=" + playerId,
				Map.class);
		Map<String, Integer> body = call.getBody();
		return body.values();
	}

	public Boolean startGame(Player player) {
		try {
			Random rand = new Random();
			int upperbound = 500;
			int int_random = rand.nextInt(upperbound);
			player.setNextValue(int_random);
			RestTemplate restTemplate = new RestTemplate();
			String writeValueAsString = new ObjectMapper().writeValueAsString(player);
			ResponseEntity<Boolean> call = restTemplate
					.getForEntity("http://localhost:5555/startGame?player=" + writeValueAsString, Boolean.class);
			return call.getBody();
		} catch (JsonProcessingException e) {
			return Boolean.FALSE;
		}
	}

}
