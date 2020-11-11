package com.takeaway_game_client.takeaway_game_client.util;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.takeaway_game_client.takeaway_game_client.constant.GameClientConstants;

public class GameClientUtils {

	public static Channel getClientChannel(Integer registerGame) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("roedeer-01.rmq.cloudamqp.com");
		factory.setVirtualHost("gqmuwpjn");
		factory.setUsername("gqmuwpjn");
		factory.setPassword("u_BRUYPkpb1wgWe_m2aPUYClMT5xeQh_");
		Connection connection;
		connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(GameClientConstants.QUEUE_NAME + registerGame.toString(), false, false, false, null);
		channel.queueBind(GameClientConstants.QUEUE_NAME + registerGame.toString(), GameClientConstants.EXCHANGE_NAME,
				registerGame.toString());
		return channel;
	}

}
