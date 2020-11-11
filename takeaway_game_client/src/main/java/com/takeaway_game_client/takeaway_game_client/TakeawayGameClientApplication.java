package com.takeaway_game_client.takeaway_game_client;

import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.takeaway_game_client.takeaway_game_client.constant.GameClientConstants;
import com.takeaway_game_client.takeaway_game_client.controller.GameClientController;
import com.takeaway_game_client.takeaway_game_client.entity.MoveState;
import com.takeaway_game_client.takeaway_game_client.entity.Player;
import com.takeaway_game_client.takeaway_game_client.util.GameClientUtils;

@SpringBootApplication
public class TakeawayGameClientApplication implements CommandLineRunner {

	@Autowired
	private RabbitTemplate template;

	@Value("${game.url}")
	private String baseUrl;

	private static ObjectMapper obj = new ObjectMapper();

	public static void main(String[] args) {
		SpringApplication.run(TakeawayGameClientApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		GameClientController gameClientController = new GameClientController();
		Integer registerGame = gameClientController.registerGame(baseUrl);// register game
		try {
			System.out.println("You registered as ----> Player " + registerGame.toString());
			Collection<Integer> activePlayers = gameClientController.getActivePlayers(registerGame, baseUrl);// get
																												// active
			// players
			if (activePlayers.size() < 1) {
				System.out.println("Currently no online players!");
				while (activePlayers.size() < 1) {
					Thread.sleep(5000);
					System.out.println("Controlling online players!");
					activePlayers = gameClientController.getActivePlayers(registerGame, baseUrl);
				}
			}
			playGame(gameClientController, activePlayers, registerGame);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	private void playGame(GameClientController gameClientController, Collection<Integer> activePlayers,
			Integer registerGame) throws IOException, TimeoutException {
		Channel channel = GameClientUtils.getClientChannel(registerGame);// queue is opened
		System.out.println("Choose one of the active players : " + activePlayers);
		Scanner scan = new Scanner(System.in); // Create a Scanner object
		String selectedRival = scan.nextLine();
		Boolean startGame = gameClientController
				.startGame(new Player(Integer.valueOf(selectedRival), registerGame, MoveState.START, null), baseUrl);
		if (Boolean.TRUE.equals(startGame)) {
			System.out.println("Game is starting with  -----> Player " + selectedRival);
			DefaultConsumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					Player player = obj.readValue(new String(body, "UTF-8"), Player.class);

					Player nextMove = new Player();
					nextMove.setPlayerId(player.getRivalId());
					nextMove.setRivalId(player.getPlayerId());

					if (player.getMoveState().equals(MoveState.END)) {
						System.out.print("Winner : Player " + player.getRivalId());
					} else {
						System.out.print("Current Value : " + player.getNextValue() + " Rival : Player "
								+ player.getRivalId() + " so select your choice in {-1, 0 , 1} : ");
						String userChoice = scan.nextLine(); // Read user input

						while (!GameClientConstants.suitNumbers.contains(userChoice)) {
							System.out.print("You should select one of them {-1, 0 , 1} : ");
							userChoice = scan.nextLine(); // Read user input
						}
						Integer nextValue = (player.getNextValue() + Integer.valueOf(userChoice)) / 3;

						if (nextValue == 1) {
							nextMove.setMoveState(MoveState.END);
							var writeValueAsString = obj.writeValueAsString(nextMove);
							template.convertAndSend("x.game", nextMove.getPlayerId().toString(), writeValueAsString);
							System.out.print("Winner : Player " + player.getPlayerId());
						} else {
							nextMove.setMoveState(MoveState.NEXT_MOVE);
							nextMove.setNextValue(nextValue);
							var writeValueAsString = obj.writeValueAsString(nextMove);
							template.convertAndSend("x.game", nextMove.getPlayerId().toString(), writeValueAsString);
						}
					}
				}
			};
			channel.basicConsume(GameClientConstants.QUEUE_NAME + registerGame.toString(), true, consumer);
		}
	}

}
