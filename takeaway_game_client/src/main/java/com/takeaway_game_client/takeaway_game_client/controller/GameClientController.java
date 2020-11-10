package com.takeaway_game_client.takeaway_game_client.controller;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.takeaway_game_client.takeaway_game_client.entity.Player;

public class GameClientController {

	private static HttpHeaders headers;

	private static JSONObject playerJsonObject;

	private static RestTemplate restTemplate;

	public Integer registerGame() {
		String generatedString = UUID.randomUUID().toString();
		restTemplate = new RestTemplate();
		ResponseEntity<String> call = restTemplate.getForEntity(
				"http://127.0.0.1:5555/register?playerId=" + generatedString.replace("-", ""), String.class);
		return Integer.valueOf(call.getBody());
	}

	public Collection<Integer> getActivePlayers(Integer playerId) {
		restTemplate = new RestTemplate();
		ResponseEntity<Map> call = restTemplate.getForEntity("http://127.0.0.1:5555/active?playerId=" + playerId,
				Map.class);
		Map<String, Integer> body = call.getBody();
		return body.values();
	}

	public Boolean startGame(Player player) {
		Random rand = new Random();
		int upperbound = 500;
		int int_random = rand.nextInt(upperbound);
		player.setNextValue(int_random);

		restTemplate = new RestTemplate();

		String url = "http://127.0.0.1:5555/start";
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		playerJsonObject = new JSONObject();
		playerJsonObject.put("playerId", player.getPlayerId());
		playerJsonObject.put("rivalId", player.getRivalId());
		playerJsonObject.put("moveState", player.getMoveState());
		playerJsonObject.put("nextValue", player.getNextValue());

		HttpEntity<String> request = new HttpEntity<String>(playerJsonObject.toString(), headers);
		ResponseEntity<Boolean> responseEntityPerson = restTemplate.postForEntity(url, request, Boolean.class);
		return responseEntityPerson.getBody();
	}

}
