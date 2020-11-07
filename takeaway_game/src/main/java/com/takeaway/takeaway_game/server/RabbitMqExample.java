package com.takeaway.takeaway_game.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqExample {

	@Autowired
	private RabbitTemplate rabbittemplate;

	public void producer() {

		InetAddress ip;
		String hostname;
		try {
			ip = InetAddress.getLocalHost();
			hostname = ip.getHostName();
			rabbittemplate.convertAndSend("x.game","1", "12313213");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

}
