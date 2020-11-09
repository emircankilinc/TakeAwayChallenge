package com.takeaway_game_client.takeaway_game_client.controller;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.takeaway_game_client.takeaway_game_client.entity.Player;

public class GameClientController {

	private static HttpHeaders headers;

	private final ObjectMapper objectMapper = new ObjectMapper();

	private static JSONObject playerJsonObject;

	private static RestTemplate restTemplate;

	public Integer registerGame() {
		byte[] array = new byte[7]; // length is bounded by 7
		new Random().nextBytes(array);
		String generatedString = new String(array, Charset.forName("UTF-8"));
		restTemplate = new RestTemplate();
		ResponseEntity<String> call = restTemplate
				.getForEntity("http://172.17.42.49:5555/register?playerId=" + generatedString, String.class);
		return Integer.valueOf(call.getBody());
	}

	public Collection<Integer> getActivePlayers(Integer playerId) {
		restTemplate = new RestTemplate();
		ResponseEntity<Map> call = restTemplate.getForEntity("http://172.17.42.49:5555/active?playerId=" + playerId,
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

		String url = "http://172.17.42.49:5555/start";
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
