package com.takeaway_game_client.takeaway_game_client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.takeaway_game_client.takeaway_game_client.controller.GameClientController;
import com.takeaway_game_client.takeaway_game_client.entity.MoveState;
import com.takeaway_game_client.takeaway_game_client.entity.Player;

@SpringBootApplication
public class TakeawayGameClientApplication implements CommandLineRunner {

	private static final String QUEUE_NAME = "q.game.";

	private List<String> suitNumbers = Arrays.asList("-1", "0", "1");

	@Autowired
	private RabbitTemplate template;

	private static ObjectMapper obj = new ObjectMapper();

	public static void main(String[] args) {
		SpringApplication.run(TakeawayGameClientApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		GameClientController gameClientController = new GameClientController();
		Integer registerGame = gameClientController.registerGame();

		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("roedeer-01.rmq.cloudamqp.com");
			factory.setVirtualHost("gqmuwpjn");
			factory.setUsername("gqmuwpjn");
			factory.setPassword("u_BRUYPkpb1wgWe_m2aPUYClMT5xeQh_");
			Connection connection;
			connection = factory.newConnection();
			Channel channel = connection.createChannel();

			channel.queueDeclare(QUEUE_NAME + registerGame.toString(), false, false, false, null);
			channel.queueBind(QUEUE_NAME + registerGame.toString(), "x.game", registerGame.toString());
			System.out.println("Queue is opened : " + QUEUE_NAME + registerGame.toString());

			DefaultConsumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					Player player = obj.readValue(message, Player.class);

					Player nextMove = new Player();
					nextMove.setPlayerId(player.getRivalId());
					nextMove.setRivalId(player.getPlayerId());

					if (player.getMoveState().equals(MoveState.END)) {
						System.out.print("Winner : Player2");
					} else {
						Scanner scan = new Scanner(System.in); // Create a Scanner object
						System.out.print("Select your choose in {-1, 0 , 1} : ");
						String userChoice = scan.nextLine(); // Read user input

						while (!suitNumbers.contains(userChoice)) {
							System.out.print("Select your choose in {-1, 0 , 1} : ");
							userChoice = scan.nextLine(); // Read user input
						}

						Integer nextValue = (player.getNextValue() + Integer.valueOf(userChoice)) / 3;

						if (nextValue == 1) {
							nextMove.setMoveState(MoveState.END);
							var writeValueAsString = obj.writeValueAsString(nextMove);
							template.convertAndSend("x.game", nextMove.getRivalId().toString(), writeValueAsString);
						} else {
							nextMove.setMoveState(MoveState.NEXT_MOVE);
							nextMove.setNextValue(nextValue);
							var writeValueAsString = obj.writeValueAsString(nextMove);
							template.convertAndSend("x.game", nextMove.getRivalId().toString(), writeValueAsString);
						}

					}
					System.out.println(" [x] Received '" + message + "'");
				}
			};
			channel.basicConsume(QUEUE_NAME + registerGame.toString(), true, consumer);
		} catch (

		IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

}
