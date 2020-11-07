package com.takeaway.takeaway_game.client;

import java.nio.charset.Charset;
import java.util.Random;

import com.takeaway.takeaway_game.service.GameService;

public class GameClient {

	public static void main(String[] args) {
		GameService service = new GameService();
		byte[] array = new byte[7]; // length is bounded by 7
		new Random().nextBytes(array);
		String generatedString = new String(array, Charset.forName("UTF-8"));
		Integer registerGame = service.registerGame(generatedString);

		
		GameClientConsumer consumer = new GameClientConsumer();
	}

}
