package com.takeaway.takeaway_game;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.takeaway.takeaway_game.entity.Player;

@RunWith(SpringRunner.class)
@SpringBootTest
class TakeawayGameApplicationTests {

	private static final String baseUrl = "http://localhost:5555/";

	@Test
	public void testRegisterService() {

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

		final ResponseEntity<String> response = restTemplate
				.getForEntity(baseUrl + "/register?playerId=" + "7fbf739023f711ebadc10242ac120002", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("1", response.getBody());
	}

	@Test
	public void testActiveService() {

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

		ResponseEntity<String> registerResponse1 = restTemplate.getForEntity(baseUrl + "/register?playerId=1",
				String.class);
		ResponseEntity<String> registerResponse2 = restTemplate.getForEntity(baseUrl + "/register?playerId=2",
				String.class);

		ResponseEntity<Map> response = restTemplate.getForEntity(baseUrl + "/active?playerId=1", Map.class);
		Map<String, Integer> body = response.getBody();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, body.size());
	}

	@Test
	public void testStartService() throws JSONException {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

		HttpHeaders headers = new HttpHeaders();
		JSONObject playerJsonObject = new JSONObject();

		Player player = new Player();

		Random rand = new Random();
		int upperbound = 500;
		int int_random = rand.nextInt(upperbound);
		player.setPlayerId(1);
		player.setRivalId(2);
		player.setNextValue(64);
		player.setNextValue(int_random);

		headers.setContentType(MediaType.APPLICATION_JSON);
		playerJsonObject.put("playerId", player.getPlayerId());
		playerJsonObject.put("rivalId", player.getRivalId());
		playerJsonObject.put("moveState", player.getMoveState());
		playerJsonObject.put("nextValue", player.getNextValue());

		HttpEntity<String> request = new HttpEntity<String>(playerJsonObject.toString(), headers);
		ResponseEntity<Boolean> response = restTemplate.postForEntity(baseUrl + "/start", request, Boolean.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(true, response.getBody());
	}

}
