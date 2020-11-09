package com.takeaway_game_client.takeaway_game_client.controller;

import java.nio.charset.Charset;
import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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

}
