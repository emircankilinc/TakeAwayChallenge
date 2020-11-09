package com.takeaway.takeaway_game.server;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.takeaway.takeaway_game.entity.Player;

@Service
public class GameServerProducer {

	@Autowired
	private RabbitTemplate rabbittemplate;

	private static ObjectMapper obj;

	public void sendStartMessage(Player player) {
		try {
			String writeValueAsString = obj.writeValueAsString(player);
			rabbittemplate.convertAndSend("x.game", player.getRivalId().toString(), writeValueAsString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

}
