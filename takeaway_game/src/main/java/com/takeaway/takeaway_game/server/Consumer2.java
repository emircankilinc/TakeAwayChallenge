package com.takeaway.takeaway_game.server;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer2 {

//	@RabbitListener(queues = "q.game.1")
//	private void consume(String deneme) {
//		System.out.println(deneme + this.getClass().toString());
//	}

}
